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
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class RulesEngine<E> implements ExecutionEngine<E> {
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
    protected final List<AbstractRule<E>> abstractRules = new CopyOnWriteArrayList<>();
    /**
     * 日志
     */
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public RulesEngine(RuleFactory ruleFactory, String businessType) {
        this.ruleFactory = ruleFactory;
        this.businessType = businessType;
        List<AbstractRule<E>> rules = this.mergeRules();
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
    private List<AbstractRule<E>> mergeRules() {
        List<AbstractRule<E>> rules = ruleFactory.findRules(businessType);
        if (ruleFactory.getValidConfiguration() != null
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
    public boolean check(E object) {
        this.checkBefore(object);
        logger.config("invoke method=check, valid object=" + object);
        for (AbstractRule<E> abstractRule : this.abstractRules) {
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
    public abstract Result execute(E object);

    @Override
    public boolean doJudge(E object, Judgement<E> judgement) {
        return judgement.isSupported(object) && judgement.judge(object);
    }

    @Override
    public Report doBuildReport(E object, Reportable<E> reportable) {
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
    protected void checkBefore(E object) {
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
     * @param <Node>         节点类型
     */
    public <Node> void registerRule(RuleInfo ruleInfo,
                                    Class<E> validClass,
                                    Class<Node> nodeClass,
                                    Function<E, Node> getNode,
                                    Function<E, Collection<Node>> getCollection,
                                    Predicate<Node> isSupported,
                                    Predicate<Node> judge,
                                    Function<Node, Set<Map.Entry<String, Object>>> collectEntries) {
        boolean isCollection = getCollection != null && getNode == null;
        DynamicRule<E, Node> rule = DynamicRuleProxy.builder(ruleInfo, validClass, nodeClass, isCollection)
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
    public void registerRule(AbstractRule<E> rule) {
        this.ruleFactory.registerRule(rule);
        this.addRule(rule);
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param rule 规则
     */
    public void addRule(AbstractRule<E> rule) {
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
        AbstractRule<E> rule = this.ruleFactory.getRule(ruleCode);
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
        for (AbstractRule<E> abstractRule : this.abstractRules) {
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

    public List<AbstractRule<E>> getRules() {
        return abstractRules;
    }
}
