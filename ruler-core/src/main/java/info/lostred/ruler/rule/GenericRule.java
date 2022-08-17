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
            this.type = (Class<T>) types[0];
        }
    }

    @Override
    public boolean judge(EvaluationContext context, ExpressionParser parser) {
        String parameterExp = this.ruleDefinition.getParameterExp();
        T value = parser.parseExpression(parameterExp).getValue(context, type);
        return this.doJudge(context, parser, value);
    }

    /**
     * 判断校验值是否满足给定条件
     *
     * @param context 评估上下文
     * @param parser  表达式解析器
     * @param value   校验值
     * @return 满足返回true，否则返回false
     */
    protected abstract boolean doJudge(EvaluationContext context, ExpressionParser parser, T value);
}
