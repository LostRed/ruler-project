package info.lostred.ruler.exception;

import info.lostred.ruler.domain.RuleDefinition;

/**
 * 规则初始化异常
 *
 * @author lostred
 */
public class RuleInitializationException extends RuntimeException {
    private final RuleDefinition ruleDefinition;

    public RuleInitializationException(RuleDefinition ruleDefinition) {
        super();
        this.ruleDefinition = ruleDefinition;
    }

    public RuleInitializationException(String message, RuleDefinition ruleDefinition) {
        super(message);
        this.ruleDefinition = ruleDefinition;
    }

    public RuleInitializationException(String message, Throwable cause, RuleDefinition ruleDefinition) {
        super(message, cause);
        this.ruleDefinition = ruleDefinition;
    }

    public RuleInitializationException(Throwable cause, RuleDefinition ruleDefinition) {
        super(cause);
        this.ruleDefinition = ruleDefinition;
    }

    public RuleInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RuleDefinition ruleDefinition) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ruleDefinition = ruleDefinition;
    }

    public RuleDefinition getRuleInfo() {
        return ruleDefinition;
    }
}
