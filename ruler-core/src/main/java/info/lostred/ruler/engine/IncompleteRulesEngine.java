package info.lostred.ruler.engine;

import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 不完全执行返回详细结果的规则引擎
 *
 * @author lostred
 */
public class IncompleteRulesEngine extends AbstractRulesEngine {
    public IncompleteRulesEngine(String businessType, RuleFactory ruleFactory, BeanResolver beanResolver,
                                 ExpressionParser expressionParser, List<Method> globalFunctions) {
        super(businessType, ruleFactory, expressionParser, beanResolver, globalFunctions);
    }

    @Override
    public void execute(EvaluationContext context) {
        for (AbstractRule rule : rules) {
            try {
                if (this.executeInternal(context, rule)) {
                    return;
                }
            } catch (Exception e) {
                throw new RulesEnginesException("rule[" + rule.getRuleDefinition().getRuleCode() + "] has occurred an exception: " + e.getMessage(), this.getBusinessType(), this.getClass());
            }
        }
    }
}
