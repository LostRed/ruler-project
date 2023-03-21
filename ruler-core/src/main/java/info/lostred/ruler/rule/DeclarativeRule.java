package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import info.lostred.ruler.core.ExecutionContextHolder;
import org.springframework.expression.ExpressionParser;

/**
 * 声明式规则
 * <p>需要定义parameterExp参数表达式，conditionExp条件表达式和predicateExp断定表达式，规则引擎根据规则的这三个表达式执行判断。</p>
 *
 * @author lostred
 * @since 2.2.0
 */
public class DeclarativeRule extends AbstractRule implements Evaluator {
    /**
     * 表达式解析器
     */
    private ExpressionParser expressionParser;

    @Override
    public boolean supports(Object object) {
        String conditionExp = this.getRuleDefinition().getConditionExp();
        Boolean flag = this.getExpressionParser()
                .parseExpression(conditionExp)
                .getValue(ExecutionContextHolder.getContext(), Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public Object getValue(Object object) {
        String parameterExp = this.getRuleDefinition().getParameterExp();
        return this.getExpressionParser()
                .parseExpression(parameterExp)
                .getValue(ExecutionContextHolder.getContext());
    }

    @Override
    public boolean evaluate(Object object) {
        String predicateExp = this.getRuleDefinition().getPredicateExp();
        Boolean flag = this.getExpressionParser()
                .parseExpression(predicateExp)
                .getValue(ExecutionContextHolder.getContext(), Boolean.class);
        return Boolean.TRUE.equals(flag);
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }
}
