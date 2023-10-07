package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.rule.AbstractRule;

/**
 * 完全执行返回详细结果的规则引擎
 *
 * @author lostred
 */
public class CompleteRulesEngine extends AbstractRulesEngine {
    @Override
    public Result execute(Object rootObject) {
        try {
            this.initContext(rootObject);
            Result result = Result.newInstance();
            for (AbstractRule rule : rules) {
                try {
                    this.executeInternal(rootObject, rule, result);
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
