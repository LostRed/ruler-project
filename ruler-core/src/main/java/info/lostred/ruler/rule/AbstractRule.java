package info.lostred.ruler.rule;

import info.lostred.ruler.core.Collector;
import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

import static info.lostred.ruler.constant.SpELConstants.INDEX;
import static info.lostred.ruler.constant.SpELConstants.INDEX_LABEL;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Judgement, Collector {
    /**
     * 规则定义
     */
    protected final RuleDefinition ruleDefinition;

    public AbstractRule(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public Map<String, Object> collectMappings(EvaluationContext context, ExpressionParser parser) {
        Map<String, Object> map = new HashMap<>();
        String parameterExp = ruleDefinition.getParameterExp();
        Object value = parser.parseExpression(parameterExp).getValue(context);
        if (parameterExp.contains(INDEX_LABEL)) {
            Object index = parser.parseExpression(INDEX).getValue(context);
            assert index != null;
            map.put(parameterExp.replace(INDEX, index.toString()), value);
        } else {
            map.put(parameterExp, value);
        }
        return map;
    }
}
