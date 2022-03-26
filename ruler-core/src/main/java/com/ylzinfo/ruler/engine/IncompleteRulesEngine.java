package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.constants.ValidGrade;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.domain.Report;

import java.util.Collection;
import java.util.Optional;

/**
 * 不完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class IncompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public IncompleteRulesEngine(String businessType, Collection<AbstractRule<E>> abstractRules) {
        super(businessType, abstractRules);
    }

    @Override
    public boolean toNext(String grade) {
        return !ValidGrade.ILLEGAL.getText().equals(grade);
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param element 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public Optional<Report> findSuspiciousReport(E element) {
        for (AbstractRule<E> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(element) && ValidGrade.SUSPECTED.getText().equals(abstractRule.getRuleInfo().getGrade())) {
                return this.ruleReport(element, abstractRule);
            }
        }
        return Optional.empty();
    }
}
