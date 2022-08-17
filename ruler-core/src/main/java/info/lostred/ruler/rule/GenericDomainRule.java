package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 泛型领域模型规则
 *
 * @param <T> 领域模型类型
 */
@SuppressWarnings("unchecked")
public abstract class GenericDomainRule<T> extends AbstractRule {
    private Class<T> domainClass;

    public GenericDomainRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            this.domainClass = (Class<T>) types[0];
        }
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.supports((T) object);
        }
        return false;
    }

    @Override
    public boolean judge(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.judge((T) object);
        }
        return false;
    }

    @Override
    public Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.collectMappings((T) object);
        }
        return new HashMap<>(0);
    }

    /**
     * 是否支持对该领域实例进行判断
     *
     * @param object 领域实例
     * @return 支持返回true，否则返回false
     */
    protected abstract boolean supports(T object);

    /**
     * 判断领域实例是否满足特定的条件
     *
     * @param object 领域实例
     * @return 满足条件返回true，否则返回false
     */
    protected abstract boolean judge(T object);

    /**
     * 收集入参object中字段与值的映射关系
     *
     * @param object 领域实例
     * @return 违规字段与值的映射关系集合
     */
    protected abstract Map<String, Object> collectMappings(T object);
}
