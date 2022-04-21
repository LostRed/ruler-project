package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.Map;

/**
 * 收集接口
 *
 * @author lostred
 */
public interface Collector {
    /**
     * 生成校验报告
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @param object  参数
     * @return 报告
     */
    Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser, Object object);
}
