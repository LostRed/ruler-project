package info.lostred.ruler.rule;

import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.ResultHandler;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import static info.lostred.ruler.constant.RulerConstants.RESULT;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Judgement, ResultHandler {
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
    public boolean judge(EvaluationContext context, ExpressionParser parser) {
        String predicateExp = ruleDefinition.getPredicateExp();
        Boolean flag = parser.parseExpression(predicateExp).getValue(context, Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public void handle(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = ruleDefinition.getParameterExp();
        Object value = parser.parseExpression(parameterExp).getValue(context);
        Result result = parser.parseExpression("#" + RESULT).getValue(context, Result.class);
        if (result != null) {
            result.addInitValue(ruleDefinition, value);
        }
    }
}
