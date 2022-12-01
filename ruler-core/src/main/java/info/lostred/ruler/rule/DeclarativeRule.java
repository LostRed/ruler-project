package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 声明式规则
 * <p>需要定义parameterExp参数表达式，conditionExp条件表达式和predicateExp断定表达式，规则引擎根据规则的这三个表达式执行判断。</p>
 *
 * @author lostred
 */
public class DeclarativeRule extends AbstractRule {
    public DeclarativeRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        String conditionExp = ruleDefinition.getConditionExp();
        Boolean flag = parser.parseExpression(conditionExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        String predicateExp = ruleDefinition.getPredicateExp();
        Boolean flag = parser.parseExpression(predicateExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public Object getInitValue(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = ruleDefinition.getParameterExp();
        return parser.parseExpression(parameterExp).getValue(context);
    }
}
