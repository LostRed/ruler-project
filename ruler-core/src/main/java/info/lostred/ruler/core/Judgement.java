package info.lostred.ruler.core;

/**
 * 判断器
 *
 * @param <E> 判断器需要判断参数类型
 * @author dengluwei
 */
public interface Judgement<E> {
    /**
     * 规则是否支持
     *
     * @param element 参数
     * @return 支持返回true，否则返回false
     */
    boolean isSupported(E element);

    /**
     * 判断参数是否违规
     *
     * @param element 参数
     * @return 违规返回true，否则返回false
     */
    boolean judge(E element);
}
