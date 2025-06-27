package info.lostred.ruler.engine;

import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;

import java.util.List;
import java.util.logging.Logger;

/**
 * 可终止的规则引擎
 *
 * @author lostred
 */
public class TerminableRulesEngine extends AbstractRulesEngine {
    private Grade terminationGrade;

    public Grade getTerminationGrade() {
        return terminationGrade;
    }

    public void setTerminationGrade(Grade terminationGrade) {
        this.terminationGrade = terminationGrade;
    }

    @Override
    public Result executeWithRules(Object rootObject, List<String> ruleCodes) {
        try {
            this.initContext(rootObject);
            Result result = Result.newInstance();
            RuleFactory ruleFactory = this.getRuleFactory();
            for (String ruleCode : ruleCodes) {
                AbstractRule rule = ruleFactory.getRule(ruleCode);
                if (rule == null) {
                    Logger logger = Logger.getLogger(this.getClass().getName());
                    logger.warning("rule[" + ruleCode + "] not found in ruleFactory");
                    continue;
                }
                try {
                    if (this.executeInternal(rootObject, rule, result)) {
                        Grade ruleGrade = rule.getRuleDefinition().getGrade();
                        if (terminationGrade.ordinal() <= ruleGrade.ordinal()) {
                            return result;
                        }
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
