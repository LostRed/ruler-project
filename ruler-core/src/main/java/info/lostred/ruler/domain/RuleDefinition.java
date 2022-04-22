package info.lostred.ruler.domain;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.constant.RulerConstants;

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
     * 规则类型
     */
    private Class<?> ruleClass;
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

    public static RuleDefinition of(Rule rule, Class<?> ruleClass) {
        return of(rule.ruleCode(), rule.businessType(), rule.grade(), rule.description(), rule.order(),
                ruleClass, rule.parameterExp(), rule.conditionExp(), rule.predicateExp());
    }

    public static RuleDefinition of(String description,
                                    Class<?> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        return new RuleDefinition(UUID.randomUUID().toString(), RulerConstants.COMMON_BUSINESS_TYPE, Grade.ILLEGAL, description, 0,
                ruleClass, parameterExp, conditionExp, predicateExp);
    }

    public static RuleDefinition of(String ruleCode, String businessType, Grade grade, String description, Integer order,
                                    Class<?> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        return new RuleDefinition(ruleCode, businessType, grade, description, order,
                ruleClass, parameterExp, conditionExp, predicateExp);
    }

    private RuleDefinition() {
    }

    public RuleDefinition(String ruleCode, String businessType, Grade grade, String description, Integer order,
                          Class<?> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        ruleCode = ruleCode == null || "".equals(ruleCode) ? UUID.randomUUID().toString() : ruleCode;
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.description = description;
        this.order = order;
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

    public Class<?> getRuleClass() {
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
