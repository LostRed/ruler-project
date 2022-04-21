package info.lostred.ruler.engine;

import info.lostred.ruler.constant.Grade;
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
    public boolean toNext(Grade grade) {
        return !Grade.ILLEGAL.equals(grade);
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param object 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public Report findSuspiciousReport(T object) {
        for (AbstractRule<T> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(object)
                    && Grade.SUSPECTED.equals(abstractRule.getRuleInfo().getGrade())) {
                return this.doBuildReport(object, abstractRule);
            }
        }
        return null;
    }
}
