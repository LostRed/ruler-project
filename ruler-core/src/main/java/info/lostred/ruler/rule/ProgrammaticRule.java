package info.lostred.ruler.rule;

/**
 * 编程式规则
 * <p>由规则的校验对象为评估上下文的根对象，作为supportsInternal、evaluateInternal和getValueInternal方法的入参，
 * 方便编程式开发。这种情况下无需定义parameterExp参数表达式、conditionExp条件表达式和predicateExp断定表达式。</p>
 *
 * @param <T> 校验值的类型
 * @author lostred
 * @since 2.2.0
 */
public abstract class ProgrammaticRule<T> extends AbstractRule {
    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Object object) {
        return this.supportsInternal((T) object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValue(Object object) {
        return this.getValueInternal((T) object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean evaluate(Object object) {
        return this.evaluateInternal((T) object);
    }

    /**
     * 规则是否支持对该参数进行判断
     *
     * @param object 转换成泛型类后的参数
     * @return 支持返回true，否则返回false
     */
    public abstract boolean supportsInternal(T object);

    /**
     * 获取需要记录的值
     *
     * @param object 转换成泛型类后的参数
     * @return 需要记录的值
     */
    public abstract Object getValueInternal(T object);

    /**
     * 评估参数是否满足特定的条件
     *
     * @param object 转换成泛型类后的参数
     * @return 满足条件返回true，否则返回false
     */
    public abstract boolean evaluateInternal(T object);
}
