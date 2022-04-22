package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.constant.RulerConstants;
import info.lostred.ruler.constant.EngineType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ruler配置类
 *
 * @author lostred
 */
@ConfigurationProperties("ruler")
public class RulerProperties {
    private String businessType = RulerConstants.COMMON_BUSINESS_TYPE;
    private RuleConfig ruleConfig = new RuleConfig();
    private RulesEngineConfig rulesEngineConfig = new RulesEngineConfig();

    /**
     * 规则配置
     */
    public static class RuleConfig {
        private String[] scanBasePackages;

        public String[] getScanBasePackages() {
            return scanBasePackages;
        }

        public void setScanBasePackages(String[] scanBasePackages) {
            this.scanBasePackages = scanBasePackages;
        }
    }

    /**
     * 规则引擎配置
     */
    public static class RulesEngineConfig {
        private String type = EngineType.SIMPLE.name();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    public void setRuleConfig(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public RulesEngineConfig getRulesEngineConfig() {
        return rulesEngineConfig;
    }

    public void setRulesEngineConfig(RulesEngineConfig rulesEngineConfig) {
        this.rulesEngineConfig = rulesEngineConfig;
    }
}
