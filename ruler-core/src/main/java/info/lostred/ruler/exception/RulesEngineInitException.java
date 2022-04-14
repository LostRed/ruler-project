package info.lostred.ruler.exception;

import info.lostred.ruler.core.RulesEngine;

/**
 * 规则引擎初始化异常
 *
 * @author lostred
 */
public class RulesEngineInitException extends RuntimeException {
    private final String businessType;
    private final Class<? extends RulesEngine<?>> rulesEngineType;

    public RulesEngineInitException(String businessType,
                                    Class<? extends RulesEngine<?>> rulesEngineType) {
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEngineInitException(String message, String businessType,
                                    Class<? extends RulesEngine<?>> rulesEngineType) {
        super(message);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEngineInitException(String message, Throwable cause, String businessType,
                                    Class<? extends RulesEngine<?>> rulesEngineType) {
        super(message, cause);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEngineInitException(Throwable cause, String businessType,
                                    Class<? extends RulesEngine<?>> rulesEngineType) {
        super(cause);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEngineInitException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace,
                                    String businessType,
                                    Class<? extends RulesEngine<?>> rulesEngineType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public Class<? extends RulesEngine<?>> getRulesEngineType() {
        return rulesEngineType;
    }
}
