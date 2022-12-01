package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 评估接口
 *
 * @author lostred
 */
public interface Evaluator {
    /**
     * 在给定的评估上下文与表达式解析器下，评估接口是否支持对该参数进行判断
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @return 支持返回true，否则返回false
     */
    boolean supports(EvaluationContext context, ExpressionParser parser);

    /**
     * 根据评估上下文与表达式解析器，评估参数是否满足特定的条件
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @return 满足条件返回true，否则返回false
     */
    boolean evaluate(EvaluationContext context, ExpressionParser parser);
}
