package info.lostred.ruler.engine;

import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;

/**
 * 简单执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public class SimpleRulesEngine<E> extends RulesEngine<E> {

    public SimpleRulesEngine(RuleFactory ruleFactory, String businessType) {
        super(ruleFactory, businessType);
    }

    @Override
    public Result execute(E object) {
        throw new UnsupportedOperationException("execute");
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param object 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public boolean hasSuspicious(E object) {
        for (AbstractRule<E> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(object) && ValidGrade.SUSPECTED.name().equals(abstractRule.getRuleInfo().getGrade())) {
                return this.doJudge(object, abstractRule);
            }
        }
        return false;
    }
}
