package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.rule.AbstractRule;

/**
 * 不完全执行返回详细结果的规则引擎
 *
 * @author lostred
 */
public class IncompleteRulesEngine extends AbstractRulesEngine {
    @Override
    public Result execute(Object rootObject) {
        try {
            this.initContext(rootObject);
            Result result = Result.newInstance();
            for (AbstractRule rule : rules) {
                try {
                    if (this.executeInternal(rootObject, rule, result)) {
                        return result;
                    }
                } catch (Exception e) {
                    String message = this.getExceptionMessage(rule, e);
                    throw new RulesEnginesException(message, e, this.getBusinessType(), this.getClass());
                }
            }
            return result;
        } finally {
            this.destroyContext();
        }
    }
}
