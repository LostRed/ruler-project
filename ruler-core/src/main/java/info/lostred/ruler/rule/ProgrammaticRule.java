package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 编程式规则
 * <p>由规则定义的parameterExp参数表达式解析出需要校验的对象，作为doSupports和doJudge方法的入参，
 * 方便编程式开发。这种情况下无需定义conditionExp条件表达式和predicateExp断定表达式。</p>
 *
 * @param <T> 校验值的类型
 * @author lostred
 */
public abstract class ProgrammaticRule<T> extends AbstractRule {
    protected Class<T> type;

    @SuppressWarnings("unchecked")
    public ProgrammaticRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            Type superType = types[0];
            if (superType instanceof Class) {
                this.type = (Class<T>) superType;
            } else if (superType instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) superType).getRawType();
                if (rawType instanceof Class) {
                    this.type = (Class<T>) rawType;
                }
            } else {
                throw new IllegalArgumentException("GenericRule must be a parameterized type");
            }
        }
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        T value = this.getGenericParameter(context, parser);
        return this.supportsInternal(value);
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        T value = this.getGenericParameter(context, parser);
        return this.evaluateInternal(value);
    }

    @Override
    public Object getInitValue(EvaluationContext context, ExpressionParser parser) {
        T value = this.getGenericParameter(context, parser);
        return this.getInitValueInternal(value);
    }

    /**
     * 解析参数表达式的值，并转换成泛型类
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @return 解析后的值
     */
    protected T getGenericParameter(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = ruleDefinition.getParameterExp();
        return parser.parseExpression(parameterExp).getValue(context, type);
    }

    /**
     * 规则是否支持对该参数进行判断
     *
     * @param value 校验值
     * @return 需要返回true，否则返回false
     */
    protected abstract boolean supportsInternal(T value);

    /**
     * 评估参数是否满足特定的条件
     *
     * @param value 校验值
     * @return 触发返回true，否则返回false
     */
    protected abstract boolean evaluateInternal(T value);

    /**
     * 从参数中获取初始值
     *
     * @param value 参数
     * @return 初始值
     */
    protected Object getInitValueInternal(T value) {
        return value;
    }
}
