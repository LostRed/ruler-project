package com.ylzinfo.ruler.domain;

/**
 * 规则信息
 *
 * @author dengluwei
 */
public class RuleInfo implements Cloneable {
    /**
     * 规则编号
     */
    private String ruleCode;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 规则校验结果等级
     */
    private String grade;
    /**
     * 规则描述
     */
    private String desc;
    /**
     * 规则执行的顺序号
     */
    private Integer seq;
    /**
     * 是否强制使用
     */
    private boolean required;
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 规则实现类的全限定类名
     */
    private String ruleClassName;
    /**
     * 规则约束类的全限定类名
     */
    private String validClassName;
    /**
     * 规则约束的类
     */
    private Class<?> validClass;

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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRuleClassName() {
        return ruleClassName;
    }

    public void setRuleClassName(String ruleClassName) {
        this.ruleClassName = ruleClassName;
    }

    public String getValidClassName() {
        return validClassName;
    }

    public void setValidClassName(String validClassName) {
        this.validClassName = validClassName;
    }

    public Class<?> getValidClass() {
        return validClass;
    }

    public void setValidClass(Class<?> validClass) {
        this.validClass = validClass;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
