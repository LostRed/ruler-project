package info.lostred.ruler.engine;

import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 不完全执行返回详细结果的规则引擎
 *
 * @author lostred
 */
public class IncompleteRulesEngine extends DetailRulesEngine {
    public IncompleteRulesEngine(RuleFactory ruleFactory, String businessType,
                                 BeanResolver beanResolver, ExpressionParser parser) {
        super(ruleFactory, businessType, beanResolver, parser);
    }

    @Override
    public Result execute(Object object) {
        StandardEvaluationContext context = new StandardEvaluationContext(object);
        this.setBeanResolver(context);
        Result result = Result.of();
        for (AbstractRule rule : rules) {
            this.handle(context, object, result, rule);
            result.updateGrade();
            if (result.getGrade().equals(Grade.ILLEGAL)) {
                break;
            }
        }
        result.statistic();
        return result;
    }
}
