package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Evaluator {
    /**
     * 规则定义
     */
    protected final RuleDefinition ruleDefinition;

    public AbstractRule(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
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

    /**
     * 解析参数表达式
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @return 解析后的值
     */
    public Object getValue(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = ruleDefinition.getParameterExp();
        return parser.parseExpression(parameterExp).getValue(context);
    }
}
