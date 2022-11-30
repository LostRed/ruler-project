package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型值规则
 *
 * @param <T> 校验值的类型
 * @author lostred
 */
@SuppressWarnings("unchecked")
public abstract class GenericRule<T> extends SpELRule {
    protected Class<T> type;

    public GenericRule(RuleDefinition ruleDefinition) {
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
    public boolean judge(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = this.ruleDefinition.getParameterExp();
        T value = parser.parseExpression(parameterExp).getValue(context, type);
        return this.doJudge(value);
    }

    /**
     * 判断校验值是否满足给定条件
     *
     * @param value 校验值
     * @return 满足返回true，否则返回false
     */
    protected abstract boolean doJudge(T value);
}
