package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 声明式规则
 * <p>需要定义parameterExp参数表达式，conditionExp条件表达式和predicateExp断定表达式，规则引擎根据规则的这三个表达式执行判断。</p>
 *
 * @author lostred
 * @since 2.2.0
 */
public class DeclarativeRule extends AbstractRule implements Evaluator {
    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        String conditionExp = this.getRuleDefinition().getConditionExp();
        Boolean flag = parser.parseExpression(conditionExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        String predicateExp = this.getRuleDefinition().getPredicateExp();
        Boolean flag = parser.parseExpression(predicateExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public Object getValue(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = this.getRuleDefinition().getParameterExp();
        return parser.parseExpression(parameterExp).getValue(context);
    }
}
