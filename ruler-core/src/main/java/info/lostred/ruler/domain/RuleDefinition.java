package info.lostred.ruler.domain;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.constant.RulerConstants;
import info.lostred.ruler.rule.AbstractRule;

import java.util.Objects;
import java.util.UUID;

/**
 * 规则定义
 *
 * @author lostred
 */
public class RuleDefinition {
    /**
     * 规则编号
     */
    private String ruleCode;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 严重等级
     */
    private Grade grade;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 规则执行的顺序号
     */
    private Integer order;
    /**
     * 是否强制使用
     */
    private boolean required;
    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 规则类型
     */
    private Class<? extends AbstractRule> ruleClass;
    /**
     * 参数表达式
     */
    private String parameterExp;
    /**
     * 条件表达式
     */
    private String conditionExp;
    /**
     * 断定表达式
     */
    private String predicateExp;

    public static RuleDefinition of(Rule rule, Class<? extends AbstractRule> ruleClass) {
        return of(rule.ruleCode(), rule.businessType(), rule.grade(), rule.description(),
                rule.order(), rule.required(), rule.enable(),
                ruleClass, rule.parameterExp(), rule.conditionExp(), rule.predicateExp());
    }

    public static RuleDefinition of(String description,
                                    Class<? extends AbstractRule> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        return RuleDefinition.of(UUID.randomUUID().toString(), RulerConstants.COMMON_BUSINESS_TYPE, Grade.ILLEGAL, description,
                0, false, true,
                ruleClass, parameterExp, conditionExp, predicateExp);
    }

    public static RuleDefinition of(String ruleCode, String businessType, Grade grade, String description,
                                    Integer order, boolean required, boolean enable,
                                    Class<? extends AbstractRule> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        return new RuleDefinition(ruleCode, businessType, grade, description,
                order, required, enable,
                ruleClass, parameterExp, conditionExp, predicateExp);
    }

    private RuleDefinition() {
    }

    public RuleDefinition(String ruleCode, String businessType, Grade grade, String description,
                          Integer order, boolean required, boolean enabled,
                          Class<? extends AbstractRule> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        ruleCode = ruleCode == null || "".equals(ruleCode) ? UUID.randomUUID().toString() : ruleCode;
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.description = description;
        this.order = order;
        this.required = required;
        this.enabled = enabled;
        this.ruleClass = ruleClass;
        this.parameterExp = parameterExp;
        this.conditionExp = conditionExp;
        this.predicateExp = predicateExp;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getDescription() {
        return description;
    }

    public Integer getOrder() {
        return order;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Class<? extends AbstractRule> getRuleClass() {
        return ruleClass;
    }

    public String getParameterExp() {
        return parameterExp;
    }

    public String getConditionExp() {
        return conditionExp;
    }

    public String getPredicateExp() {
        return predicateExp;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleDefinition ruleDefinition = (RuleDefinition) o;
        return Objects.equals(ruleCode, ruleDefinition.ruleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleCode);
    }
}
