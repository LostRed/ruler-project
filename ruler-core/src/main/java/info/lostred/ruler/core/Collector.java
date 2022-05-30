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
     * 收集入参object中违规字段与值的映射关系
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @param object  参数
     * @return 违规字段与值的映射关系集合
     */
    Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser, Object object);
}
