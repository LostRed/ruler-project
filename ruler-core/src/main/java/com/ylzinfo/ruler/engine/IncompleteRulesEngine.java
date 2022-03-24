package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.constants.ValidGrade;

import java.util.Collection;
import java.util.Optional;

/**
 * 不完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class IncompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public IncompleteRulesEngine(Collection<Rule<E>> rules) {
        super(rules);
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
        for (Rule<E> rule : this.rules) {
            if (rule.isSupported(element) && ValidGrade.SUSPECTED.getText().equals(rule.getRuleInfo().getGrade())) {
                return this.ruleReport(element, rule);
            }
        }
        return Optional.empty();
    }
}
