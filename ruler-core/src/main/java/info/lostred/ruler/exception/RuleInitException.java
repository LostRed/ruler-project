package info.lostred.ruler.exception;

import info.lostred.ruler.domain.RuleInfo;

/**
 * 规则初始化异常
 *
 * @author lostred
 */
public class RuleInitException extends RuntimeException {
    private final RuleInfo ruleInfo;

    public RuleInitException(RuleInfo ruleInfo) {
        super();
        this.ruleInfo = ruleInfo;
    }

    public RuleInitException(String message, RuleInfo ruleInfo) {
        super(message);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitException(String message, Throwable cause, RuleInfo ruleInfo) {
        super(message, cause);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitException(Throwable cause, RuleInfo ruleInfo) {
        super(cause);
        this.ruleInfo = ruleInfo;
    }

    public RuleInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RuleInfo ruleInfo) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ruleInfo = ruleInfo;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }
}
