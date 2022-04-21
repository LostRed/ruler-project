package info.lostred.ruler.engine;

import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;

/**
 * 不完整执行的规则引擎
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
public class IncompleteRulesEngine<T> extends DetailRulesEngine<T> {

    public IncompleteRulesEngine(RuleFactory ruleFactory, String businessType) {
        super(ruleFactory, businessType);
    }

    @Override
    public boolean toNext(String grade) {
        return !ValidGrade.ILLEGAL.name().equals(grade);
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param object 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public Report findSuspiciousReport(T object) {
        for (AbstractRule<T> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(object) && ValidGrade.SUSPECTED.name().equals(abstractRule.getRuleInfo().getGrade())) {
                return this.doBuildReport(object, abstractRule);
            }
        }
        return null;
    }
}
