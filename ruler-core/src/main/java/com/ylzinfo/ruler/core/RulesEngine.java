package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 抽象规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public abstract class RulesEngine<E> implements ExecutionEngine<E> {
    /**
     * 业务类型
     */
    private final String businessType;
    /**
     * 规则集合
     */
    protected final LinkedList<Rule<E>> rules = new LinkedList<>();

    public RulesEngine(Collection<Rule<E>> rules) {
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("The parameter 'rules' is empty.");
        }
        this.businessType = rules.iterator().next().getRuleInfo().getBusinessType();
        this.rules.addAll(rules);
    }

    @Override
    public boolean ruleJudge(E element, Rule<E> rule) {
        if (rule.isSupported(element)) {
            return rule.judge(element);
        }
        return false;
    }

    @Override
    public Optional<Report> ruleReport(E element, Rule<E> rule) {
        if (rule.isSupported(element)) {
            Report report = rule.buildReport(element);
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
     * @param rule 规则
     */
    public void addRule(Rule<E> rule) {
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.get(i).getRuleInfo().getSeq() > rule.getRuleInfo().getSeq()) {
                this.rules.add(i, rule);
                break;
            }
        }
    }

    /**
     * 添加规则并按顺序号排序
     *
     * @param rules 规则集合
     */
    public void addRule(Collection<Rule<E>> rules) {
        rules.forEach(this::addRule);
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
        return this.rules.removeIf(rule -> ruleCode.equals(rule.getRuleInfo().getRuleCode()));
    }

    /**
     * 将规则转换规则信息列表
     *
     * @return 规则信息列表
     */
    public List<RuleInfo> getRuleInfos() {
        return this.rules.stream().map(Rule::getRuleInfo).collect(Collectors.toList());
    }

    public String getBusinessType() {
        return businessType;
    }

    public LinkedList<Rule<E>> getRules() {
        return rules;
    }
}
