package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.constants.RulesEngineType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ruler默认规则引擎实例配置类
 */
@ConfigurationProperties("ruler")
public class RulerProperties {
    private String defaultBusinessType;
    private Class<?> defaultValidClass;
    private ValidConfig validConfig = new ValidConfig();
    private RuleConfig ruleConfig = new RuleConfig();
    private RulesEngineConfig rulesEngineConfig = new RulesEngineConfig();

    static class ValidConfig {
        private String tableName = "ruler_valid_info";

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }

    static class RuleConfig {
        private String tableName = "ruler_rule_info";
        private String[] scanBasePackages;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String[] getScanBasePackages() {
            return scanBasePackages;
        }

        public void setScanBasePackages(String[] scanBasePackages) {
            this.scanBasePackages = scanBasePackages;
        }
    }

    static class RulesEngineConfig {
        private String type = RulesEngineType.SIMPLE.name();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String getDefaultBusinessType() {
        return defaultBusinessType;
    }

    public void setDefaultBusinessType(String defaultBusinessType) {
        this.defaultBusinessType = defaultBusinessType;
    }

    public Class<?> getDefaultValidClass() {
        return defaultValidClass;
    }

    public void setDefaultValidClass(Class<?> defaultValidClass) {
        this.defaultValidClass = defaultValidClass;
    }

    public ValidConfig getValidConfig() {
        return validConfig;
    }

    public void setValidConfig(ValidConfig validConfig) {
        this.validConfig = validConfig;
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
