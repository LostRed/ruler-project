package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.constant.RulerConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ruler配置类
 *
 * @author lostred
 */
@ConfigurationProperties("ruler")
public class RulerProperties {
    private String businessType = RulerConstants.COMMON_BUSINESS_TYPE;
    private String engineType = EngineType.SIMPLE.name();
    private String[] ruleDefaultScope;
    private String[] domainDefaultScope;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String[] getRuleDefaultScope() {
        return ruleDefaultScope;
    }

    public void setRuleDefaultScope(String[] ruleDefaultScope) {
        this.ruleDefaultScope = ruleDefaultScope;
    }

    public String[] getDomainDefaultScope() {
        return domainDefaultScope;
    }

    public void setDomainDefaultScope(String[] domainDefaultScope) {
        this.domainDefaultScope = domainDefaultScope;
    }
}
