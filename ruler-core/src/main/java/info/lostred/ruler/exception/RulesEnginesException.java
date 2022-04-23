package info.lostred.ruler.exception;

/**
 * 规则引擎异常
 *
 * @author lostred
 */
public class RulesEnginesException extends RuntimeException {
    private final String businessType;
    private final Class<?> rulesEngineType;

    public RulesEnginesException(String businessType,
                                 Class<?> rulesEngineType) {
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEnginesException(String message, String businessType,
                                 Class<?> rulesEngineType) {
        super(message);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEnginesException(String message, Throwable cause, String businessType,
                                 Class<?> rulesEngineType) {
        super(message, cause);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEnginesException(Throwable cause, String businessType,
                                 Class<?> rulesEngineType) {
        super(cause);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public RulesEnginesException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace,
                                 String businessType,
                                 Class<?> rulesEngineType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.businessType = businessType;
        this.rulesEngineType = rulesEngineType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public Class<?> getRulesEngineType() {
        return rulesEngineType;
    }
}
