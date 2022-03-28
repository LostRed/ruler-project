package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.factory.RuleFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
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
    protected final LinkedList<AbstractRule<E>> abstractRules = new LinkedList<>();

    public RulesEngine(RuleFactory ruleFactory, String businessType, Collection<AbstractRule<E>> abstractRules) {
        this.ruleFactory = ruleFactory;
        this.businessType = businessType;
        this.abstractRules.addAll(abstractRules);
        this.abstractRules.sort(Comparator.comparing(e -> e.getRuleInfo().getSeq()));
    }

    /**
     * 执行规则，生成是否违规的结果
     *
     * @param element 规则约束的对象
     * @return 违规返回true，否则返回false
     */
    public abstract boolean check(E element);

    /**
     * 执行规则，生成结果
     *
     * @param element 规则约束的对象
     * @return 结果
     */
    public abstract Result execute(E element);

    @Override
    public boolean ruleJudge(E element, AbstractRule<E> abstractRule) {
        if (abstractRule.isSupported(element)) {
            return abstractRule.judge(element);
        }
        return false;
    }

    @Override
    public Optional<Report> ruleReport(E element, AbstractRule<E> abstractRule) {
        if (abstractRule.isSupported(element)) {
            Report report = abstractRule.buildReport(element);
            if (report != null && !report.getIllegals().isEmpty()) {
                return Optional.of(report);
            }
        }
        return Optional.empty();
    }

    /**
     * 执行前检查
     *
     * @param element 规则约束的对象
     */
    protected void checkBeforeExecute(E element) {
        if (element == null) {
            throw new NullPointerException("The valid node is null.");
        }
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param ruleCode 规则编号
     */
    public void addRule(String ruleCode) {
        for (int i = 0; i < this.abstractRules.size(); i++) {
            AbstractRule<E> rule = this.ruleFactory.getRule(ruleCode);
            if (this.abstractRules.get(i).getRuleInfo().getSeq() > rule.getRuleInfo().getSeq()) {
                this.abstractRules.add(i, rule);
                break;
            }
        }
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

    public LinkedList<AbstractRule<E>> getRules() {
        return abstractRules;
    }
}
