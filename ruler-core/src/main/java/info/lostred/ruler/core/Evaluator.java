package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;

/**
 * 评估接口
 *
 * @author lostred
 * @since 3.0.0
 */
public interface Evaluator {
    /**
     * 根据评估上下文与表达式解析器，评估参数是否满足特定的条件
     *
     * @param context 评估上下文
     * @return 满足条件返回true，否则返回false
     */
    boolean evaluate(EvaluationContext context);
}
