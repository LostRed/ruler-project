package info.lostred.ruler.core;

/**
 * 判断器接口
 *
 * @param <T> 判断器接口需要判断参数类型
 * @author lostred
 */
public interface Judgement<T> {
    /**
     * 判断器接口是否需要判断参数
     *
     * @param object 参数
     * @return 需要返回true，否则返回false
     */
    boolean isSupported(T object);

    /**
     * 判断参数是否满足特定的条件
     *
     * @param object 参数
     * @return 满足条件返回true，否则返回false
     */
    boolean judge(T object);
}
