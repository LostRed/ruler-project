package info.lostred.ruler.rule;

/**
 * 简单规则
 * <p>不需要重写evaluateInternal方法的规则，这些规则直接通过getValueInternal方法获取记录值，返回null表示没有违反规则。</p>
 *
 * @param <T> 校验值的类型
 * @author lostred
 * @since 3.1.0
 */
public abstract class SimpleRule<T> extends ProgrammaticRule<T> {
    @Override
    public final boolean evaluateInternal(T object) {
        throw new UnsupportedOperationException();
    }
}
