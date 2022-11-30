package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 结果处理器接口
 *
 * @author LostRed
 */
public interface ResultHandler {
    /**
     * 对评估上下文中的结果参数进行处理，上下文中默认的结果表达式为#result
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     */
    void handle(EvaluationContext context, ExpressionParser parser);
}
