package info.lostred.ruler.exception;

import info.lostred.ruler.domain.RuleDefinition;

/**
 * 规则异常
 *
 * @author lostred
 */
public class RulesException extends RuntimeException {
    private final RuleDefinition ruleDefinition;

    public RulesException(RuleDefinition ruleDefinition) {
        super();
        this.ruleDefinition = ruleDefinition;
    }

    public RulesException(String message, RuleDefinition ruleDefinition) {
        super(message);
        this.ruleDefinition = ruleDefinition;
    }

    public RulesException(String message, Throwable cause, RuleDefinition ruleDefinition) {
        super(message, cause);
        this.ruleDefinition = ruleDefinition;
    }

    public RulesException(Throwable cause, RuleDefinition ruleDefinition) {
        super(cause);
        this.ruleDefinition = ruleDefinition;
    }

    public RulesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RuleDefinition ruleDefinition) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ruleDefinition = ruleDefinition;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
