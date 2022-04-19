package info.lostred.ruler.exception;

import info.lostred.ruler.domain.RuleInfo;

/**
 * 规则初始化异常
 *
 * @author lostred
 */
public class RuleInitializationException extends RuntimeException {
    private final RuleInfo ruleInfo;

    public RuleInitializationException(RuleInfo ruleInfo) {
        super();
        this.ruleInfo = ruleInfo;
    }

    public RuleInitializationException(String message, RuleInfo ruleInfo) {
        super(message);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitializationException(String message, Throwable cause, RuleInfo ruleInfo) {
        super(message, cause);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitializationException(Throwable cause, RuleInfo ruleInfo) {
        super(cause);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RuleInfo ruleInfo) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ruleInfo = ruleInfo;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }
}
