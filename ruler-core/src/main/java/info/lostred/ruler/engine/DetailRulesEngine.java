package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static info.lostred.ruler.constant.SpELConstants.INDEX_LABEL;

/**
 * 返回详细结果的规则引擎
 *
 * @author lostred
 */
public abstract class DetailRulesEngine extends AbstractRulesEngine {
    public DetailRulesEngine(RuleFactory ruleFactory, String businessType,
                             BeanResolver beanResolver, ExpressionParser parser) {
        super(ruleFactory, businessType, beanResolver, parser);
    }

    /**
     * 针对详细结果的处理
     *
     * @param context 评估上下文
     * @param object  待校验的对象
     * @param result  引擎执行的结果
     * @param rule    当前规则
     */
    protected void handle(StandardEvaluationContext context, Object object, Result result, AbstractRule rule) {
        try {
            String parameterExp = rule.getRuleDefinition().getParameterExp();
            if (parameterExp.contains(INDEX_LABEL)) {
                String arrayExp = parameterExp.substring(0, parameterExp.indexOf(INDEX_LABEL));
                Object[] array = parser.parseExpression(arrayExp).getValue(context, Object[].class);
                this.executeForArray(context, array, rule, result);
            } else {
                this.executeForObject(context, object, rule, result);
            }
        } catch (ExpressionException e) {
            String ruleCode = rule.getRuleDefinition().getRuleCode();
            this.forceRemoveRule(ruleCode);
            throw new RulesException("There are invalid expressions in rule [" + ruleCode + "], " +
                    "it had be destroyed by rule engine.", e, rule.getRuleDefinition());
        }
    }
}
