package info.lostred.ruler.engine;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.Reportable;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.exception.RulesEngineInitializationException;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.proxy.DynamicRuleProxy;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.DynamicRule;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 抽象规则引擎
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
public abstract class RulesEngine<T> implements ExecutionEngine<T> {
    /**
     * 规则工厂
     */
    private final RuleFactory ruleFactory;
    /**
     * 业务类型
     */
    private final String businessType;
    /**
     * 规则集合
     */
    protected final List<AbstractRule<T>> abstractRules = new CopyOnWriteArrayList<>();
    /**
     * 日志
     */
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public RulesEngine(RuleFactory ruleFactory, String businessType) {
        this.ruleFactory = ruleFactory;
        this.businessType = businessType;
        List<AbstractRule<T>> rules = this.mergeRules();
        if (rules.isEmpty()) {
            throw new RulesEngineInitializationException("This engine's business type is '" + businessType + "', has not available rules.",
                    this.businessType, this.getClass());
        }
        this.abstractRules.addAll(rules);
        this.abstractRules.sort(Comparator.comparing(e -> e.getRuleInfo().getSeq()));
    }

    /**
     * 将该引擎的业务规则与common业务规则合并
     *
     * @return 规则集合
     */
    private List<AbstractRule<T>> mergeRules() {
        List<AbstractRule<T>> rules = ruleFactory.findRules(businessType);
        if (ruleFactory.getValidConfiguration().isEnableCommonRules()
                && ruleFactory.getValidConfiguration() != null
                && !RulerConstants.DEFAULT_BUSINESS_TYPE.equals(businessType)) {
            rules.addAll(ruleFactory.findRules(RulerConstants.DEFAULT_BUSINESS_TYPE));
        }
        return rules;
    }

    /**
     * 执行规则，生成是否违规的结果
     *
     * @param object 规则约束的对象
     * @return 违规返回true，否则返回false
     */
    public boolean check(T object) {
        this.checkBefore(object);
        logger.config("invoke method=check, valid object=" + object);
        for (AbstractRule<T> abstractRule : this.abstractRules) {
            if (this.doJudge(object, abstractRule)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行规则，生成结果
     *
     * @param object 规则约束的对象
     * @return 结果
     */
    public abstract Result execute(T object);

    @Override
    public boolean doJudge(T object, Judgement<T> judgement) {
        return judgement.isSupported(object) && judgement.judge(object);
    }

    @Override
    public Report doBuildReport(T object, Reportable<T> reportable) {
        Report report = reportable.buildReport(object);
        if (report != null && !report.getIllegals().isEmpty()) {
            return report;
        }
        return null;
    }

    /**
     * 执行前检查
     *
     * @param object 规则约束的对象
     */
    protected void checkBefore(T object) {
        if (object == null) {
            throw new NullPointerException("The valid node is null.");
        }
    }

    /**
     * 注册规则
     *
     * @param ruleInfo       规则信息
     * @param getNode        获取单个校验节点的函数
     * @param getCollection  获取多个校验节点的集合的函数
     * @param isSupported    规则是否支持的断定
     * @param judge          校验结果为违规的断定
     * @param collectEntries 收集非法键值对的函数
     * @param validClass     规则约束类的类对象
     * @param nodeClass      节点类的类对象
     * @param <E>            节点类型
     */
    public <E> void registerRule(RuleInfo ruleInfo,
                                 Class<T> validClass,
                                 Class<E> nodeClass,
                                 Function<T, E> getNode,
                                 Function<T, Collection<E>> getCollection,
                                 Predicate<E> isSupported,
                                 Predicate<E> judge,
                                 Function<E, Set<Map.Entry<String, Object>>> collectEntries) {
        boolean isCollection = getCollection != null && getNode == null;
        DynamicRule<T, E> rule = DynamicRuleProxy.builder(ruleInfo, validClass, nodeClass, isCollection)
                .setGetNode(getNode)
                .setGetCollection(getCollection)
                .setIsSupported(isSupported)
                .setJudge(judge)
                .setCollectEntries(collectEntries)
                .build();
        this.registerRule(rule);
    }

    /**
     * 注册规则
     *
     * @param rule 规则
     */
    public void registerRule(AbstractRule<T> rule) {
        this.ruleFactory.registerRule(rule);
        this.addRule(rule);
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param rule 规则
     */
    public void addRule(AbstractRule<T> rule) {
        for (int i = 0; i < this.abstractRules.size(); i++) {
            if (this.abstractRules.get(i).getRuleInfo().getSeq() > rule.getRuleInfo().getSeq()) {
                this.abstractRules.add(i, rule);
                break;
            }
        }
        this.abstractRules.add(rule);
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param ruleCode 规则编号
     */
    public void addRule(String ruleCode) {
        AbstractRule<T> rule = this.ruleFactory.getRule(ruleCode);
        this.addRule(rule);
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param ruleCodes 规则编号集合
     */
    public void addRule(Collection<String> ruleCodes) {
        ruleCodes.forEach(this::addRule);
    }

    /**
     * 移除规则
     *
     * @param ruleCode 规则编号
     * @return 成功移除返回true，否则返回false
     */
    public boolean removeRule(String ruleCode) {
        if (ruleCode == null) {
            return false;
        }
        for (AbstractRule<T> abstractRule : this.abstractRules) {
            if (abstractRule.getRuleInfo().isRequired()) {
                throw new RuntimeException("Cannot remove required rule.");
            } else {
                return this.abstractRules.remove(abstractRule);
            }
        }
        return false;
    }

    /**
     * 将规则转换规则信息列表
     *
     * @return 规则信息列表
     */
    public List<RuleInfo> getRuleInfos() {
        return this.abstractRules.stream().map(AbstractRule::getRuleInfo).collect(Collectors.toList());
    }

    public String getBusinessType() {
        return businessType;
    }

    public List<AbstractRule<T>> getRules() {
        return abstractRules;
    }
}
