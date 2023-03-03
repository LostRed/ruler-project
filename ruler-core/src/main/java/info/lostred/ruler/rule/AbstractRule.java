package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Evaluator {
    /**
     * 规则定义
     */
    private RuleDefinition ruleDefinition;
    /**
     * 表达式解析器
     */
    private ExpressionParser expressionParser;

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public void setRuleDefinition(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    /**
     * 在给定的评估上下文与表达式解析器下，评估接口是否支持对该参数进行判断
     *
     * @param context 评估上下文
     * @return 支持返回true，否则返回false
     */
    public abstract boolean supports(EvaluationContext context);

    /**
     * 获取需要记录的值
     *
     * @param context 评估上下文
     * @return 需要记录的值
     */
    public abstract Object getValue(EvaluationContext context);
}
