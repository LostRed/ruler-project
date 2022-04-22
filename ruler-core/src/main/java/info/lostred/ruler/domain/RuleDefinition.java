package info.lostred.ruler.domain;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constant.Grade;

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
    private Integer sequence;
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
    /**
     * 是否在集合参数中操作元素
     */
    private boolean opsInCollection;

    public static RuleDefinition of(Rule rule, Class<?> ruleClass) {
        return of(UUID.randomUUID().toString(), rule.businessType(), rule.grade(), rule.description(),
                ruleClass, rule.parameterExp(), rule.conditionExp(), rule.predicateExp(), rule.seq());
    }

    public static RuleDefinition of(String ruleCode, String businessType, Grade grade, String description,
                                    Class<?> ruleClass, String parameterExp, String conditionExp, String predicateExp, Integer seq) {
        return new RuleDefinition(ruleCode, businessType, grade, description, seq, ruleClass, parameterExp, conditionExp, predicateExp);
    }

    private RuleDefinition() {
    }

    public RuleDefinition(String ruleCode, String businessType, Grade grade, String description, Integer sequence,
                          Class<?> ruleClass, String parameterExp, String conditionExp, String predicateExp) {
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.description = description;
        this.sequence = sequence;
        this.ruleClass = ruleClass;
        this.parameterExp = parameterExp;
        this.conditionExp = conditionExp;
        this.predicateExp = predicateExp;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Class<?> getRuleClass() {
        return ruleClass;
    }

    public void setRuleClass(Class<?> ruleClass) {
        this.ruleClass = ruleClass;
    }

    public String getParameterExp() {
        return parameterExp;
    }

    public void setParameterExp(String parameterExp) {
        this.parameterExp = parameterExp;
    }

    public String getConditionExp() {
        return conditionExp;
    }

    public void setConditionExp(String conditionExp) {
        this.conditionExp = conditionExp;
    }

    public String getPredicateExp() {
        return predicateExp;
    }

    public void setPredicateExp(String predicateExp) {
        this.predicateExp = predicateExp;
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
