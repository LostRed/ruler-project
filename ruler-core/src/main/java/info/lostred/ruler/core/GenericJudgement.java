package info.lostred.ruler.core;

/**
 * 泛型参数判断器接口
 *
 * @param <T> 参数类型
 * @author lostred
 */
public interface GenericJudgement<T> {
    /**
     * 在给定的评估上下文与表达式解析器下，判断器接口是否支持对该参数进行判断
     *
     * @param object 参数
     * @return 支持返回true，否则返回false
     */
    boolean supports(T object);

    /**
     * 根据评估上下文与表达式解析器，判断参数是否满足特定的条件
     *
     * @param object 参数
     * @return 满足条件返回true，否则返回false
     */
    boolean judge(T object);
}
