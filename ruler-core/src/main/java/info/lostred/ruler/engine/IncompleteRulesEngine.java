package info.lostred.ruler.engine;

import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.factory.RuleFactory;

import java.util.Collection;

/**
 * 不完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public class IncompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public IncompleteRulesEngine(RuleFactory ruleFactory, String businessType, Collection<AbstractRule<E>> abstractRules) {
        super(ruleFactory, businessType, abstractRules);
    }

    @Override
    public boolean toNext(String grade) {
        return !ValidGrade.ILLEGAL.name().equals(grade);
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param element 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public Report findSuspiciousReport(E element) {
        for (AbstractRule<E> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(element) && ValidGrade.SUSPECTED.name().equals(abstractRule.getRuleInfo().getGrade())) {
                return this.doBuildReport(element, abstractRule);
            }
        }
        return null;
    }
}
