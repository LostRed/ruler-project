package info.lostred.ruler.engine;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.IterationEngine;
import info.lostred.ruler.core.RulesEngine;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;

import java.util.Collection;
import java.util.Iterator;

/**
 * 详细执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class DetailRulesEngine<E> extends RulesEngine<E> implements IterationEngine {

    public DetailRulesEngine(RuleFactory ruleFactory, String businessType, Collection<AbstractRule<E>> abstractRules) {
        super(ruleFactory, businessType, abstractRules);
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
