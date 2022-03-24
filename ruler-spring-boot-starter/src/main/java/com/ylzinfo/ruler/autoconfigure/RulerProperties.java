package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.constants.RulesEngineType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ruler默认规则引擎实例配置类
 *
 * @param <E> 规则约束的参数类型
 */
@ConfigurationProperties("ruler")
public class RulerProperties<E> {
    private String ruleInfoTableName = "ruler_rule_info";
    private String validInfoTableName = "ruler_valid_info";
    private String defaultRulesEngineType = RulesEngineType.SIMPLE.toString();
    private String defaultBusinessType;
    private Class<E> defaultValidClass;

    public String getRuleInfoTableName() {
        return ruleInfoTableName;
    }

    public void setRuleInfoTableName(String ruleInfoTableName) {
        this.ruleInfoTableName = ruleInfoTableName;
    }

    public String getValidInfoTableName() {
        return validInfoTableName;
    }

    public void setValidInfoTableName(String validInfoTableName) {
        this.validInfoTableName = validInfoTableName;
    }

    public String getDefaultRulesEngineType() {
        return defaultRulesEngineType;
    }

    public void setDefaultRulesEngineType(String defaultRulesEngineType) {
        this.defaultRulesEngineType = defaultRulesEngineType;
    }

    public String getDefaultBusinessType() {
        return defaultBusinessType;
    }

    public void setDefaultBusinessType(String defaultBusinessType) {
        this.defaultBusinessType = defaultBusinessType;
    }

    public Class<E> getDefaultValidClass() {
        return defaultValidClass;
    }

    public void setDefaultValidClass(Class<E> defaultValidClass) {
        this.defaultValidClass = defaultValidClass;
    }
}
