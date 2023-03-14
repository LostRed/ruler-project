package info.lostred.ruler.engine;

import info.lostred.ruler.core.RulerContextHolder;
import info.lostred.ruler.domain.Result;
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
                    if (this.executeInternal(RulerContextHolder.getContext(), rule, result)) {
                        return result;
                    }
                } catch (Exception e) {
                    throw new RulesEnginesException("rule[" + rule.getRuleDefinition().getRuleCode() + "] has occurred an exception: " + e.getMessage(), this.getBusinessType(), this.getClass());
                }
            }
            return result;
        } finally {
            RulerContextHolder.clear();
        }
    }
}
