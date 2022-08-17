package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

import static info.lostred.ruler.constant.SpELConstants.INDEX;
import static info.lostred.ruler.constant.SpELConstants.INDEX_LABEL;

/**
 * SpEL规则
 *
 * @author lostred
 */
public class SpELRule extends AbstractRule {
    public SpELRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        String conditionExp = ruleDefinition.getConditionExp();
        Boolean flag = parser.parseExpression(conditionExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public boolean judge(EvaluationContext context, ExpressionParser parser) {
        String predicateExp = ruleDefinition.getPredicateExp();
        Boolean flag = parser.parseExpression(predicateExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser) {
        Map<String, Object> map = new HashMap<>();
        String parameterExp = ruleDefinition.getParameterExp();
        Object value = parser.parseExpression(parameterExp).getValue(context);
        if (parameterExp.contains(INDEX_LABEL)) {
            Object index = parser.parseExpression(INDEX).getValue(context);
            assert index != null;
            map.put(parameterExp.replace(INDEX, index.toString()), value);
        } else {
            map.put(parameterExp, value);
        }
        return map;
    }
}