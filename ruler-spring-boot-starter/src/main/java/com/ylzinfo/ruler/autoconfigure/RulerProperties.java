package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.constants.RulerConstants;
import com.ylzinfo.ruler.constants.RulesEngineType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ruler配置类
 *
 * @author dengluwei
 */
@ConfigurationProperties("ruler")
public class RulerProperties {
    private String defaultBusinessType = RulerConstants.DEFAULT_BUSINESS_TYPE;
    private Class<?> defaultValidClass;
    private DbConfig dbConfig = new DbConfig();
    private ValidConfig validConfig = new ValidConfig();
    private RuleConfig ruleConfig = new RuleConfig();
    private RulesEngineConfig rulesEngineConfig = new RulesEngineConfig();

    /**
     * 数据库配置
     */
    public static class DbConfig {
        private String driverClassName;
        private String url;
        private String username;
        private String password;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 校验配置
     */
    public static class ValidConfig {
        private String tableName = RulerConstants.ORIGIN_VALID_INFO_TABLE_NAME;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }

    /**
     * 规则配置
     */
    public static class RuleConfig {
        private String tableName = RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME;
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

    /**
     * 规则引擎配置
     */
    public static class RulesEngineConfig {
        private String type = RulesEngineType.SIMPLE.name();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
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
