package info.lostred.ruler.core;

import java.util.Map;

/**
 * 泛型参数收集接口
 *
 * @param <T> 参数类型
 * @author lostred
 */
public interface GenericCollector<T> {
    /**
     * 根据评估上下文与表达式解析器，收集入参object中字段与值的映射关系
     *
     * @param object 参数
     * @return 违规字段与值的映射关系集合
     */
    Map<String, Object> collectMappings(T object);
}
