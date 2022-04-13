package info.lostred.ruler.engine;

import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.factory.RuleFactory;

import java.util.Collection;
import java.util.Optional;

/**
 * 不完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class IncompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public IncompleteRulesEngine(RuleFactory ruleFactory, String businessType, Collection<AbstractRule<E>> abstractRules) {
        super(ruleFactory, businessType, abstractRules);
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
