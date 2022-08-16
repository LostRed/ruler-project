package info.lostred.ruler.rule;

import info.lostred.ruler.core.GenericCollector;
import info.lostred.ruler.core.GenericJudgement;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 泛型领域模型规则
 *
 * @param <T> 领域模型类型
 */
public abstract class GenericDomainRule<T> extends AbstractRule implements GenericJudgement<T>, GenericCollector<T> {
    private final Class<T> domainClass;

    public GenericDomainRule(RuleDefinition ruleDefinition, Class<T> domainClass) {
        super(ruleDefinition);
        this.domainClass = domainClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.supports((T) object);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean judge(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.judge((T) object);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser, Object object) {
        if (domainClass.isAssignableFrom(object.getClass())) {
            return this.collectMappings((T) object);
        }
        return new HashMap<>(0);
    }
}
