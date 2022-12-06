package info.lostred.ruler.engine;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 完全执行返回详细结果的规则引擎
 *
 * @author lostred
 */
public class CompleteRulesEngine extends AbstractRulesEngine {
    public CompleteRulesEngine(String businessType, RuleFactory ruleFactory, ExpressionParser expressionParser,
                               BeanResolver beanResolver, List<Method> globalFunctions) {
        super(businessType, ruleFactory, expressionParser, beanResolver, globalFunctions);
    }

    @Override
    public void execute(EvaluationContext context) {
        for (AbstractRule rule : rules) {
            try {
                this.executeInternal(context, rule);
            } catch (Exception e) {
                RuleDefinition ruleDefinition = rule.getRuleDefinition();
                throw new RulesEnginesException("rule[" + ruleDefinition.getRuleCode() + " " + ruleDefinition.getGrade() + "] has occurred an exception: " + e.getMessage(), this.getBusinessType(), this.getClass());
            }
        }
    }
}
