package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;

import java.util.Iterator;

/**
 * 详细执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class DetailRulesEngine<E> extends RulesEngine<E> implements IterationEngine {

    public DetailRulesEngine(RuleFactory ruleFactory, String businessType) {
        super(ruleFactory, businessType);
    }

    @Override
    public Result execute(E object) {
        super.checkBefore(object);
        logger.config("invoke method=execute, valid object=" + object);
        Result result = Result.of();
        Iterator<AbstractRule<E>> iterator = this.abstractRules.iterator();
        while (iterator.hasNext() && this.toNext(result.getGrade())) {
            AbstractRule<E> abstractRule = iterator.next();
            if (abstractRule.isSupported(object)) {
                Report report = this.doBuildReport(object, abstractRule);
                if (report != null) {
                    result.addReport(report);
                    result.updateGrade(report);
                }
            }
        }
        return result.statistic();
    }
}
