package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

/**
 * 不完全执行返回简单的规则引擎
 *
 * @author lostred
 */
public class SimpleRulesEngine extends AbstractRulesEngine {
    public SimpleRulesEngine(RuleFactory ruleFactory, String businessType,
                             BeanResolver beanResolver, ExpressionParser parser) {
        super(ruleFactory, businessType, beanResolver, parser);
    }

    @Override
    public Result execute(Object object) {
        throw new UnsupportedOperationException("execute");
    }
}
