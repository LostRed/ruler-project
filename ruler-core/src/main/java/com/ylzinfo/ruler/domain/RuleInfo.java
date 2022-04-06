package com.ylzinfo.ruler.domain;

import com.ylzinfo.ruler.annotation.Rule;

import java.util.Objects;

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

    public static RuleInfo of(Rule rule, Class<?> ruleClass) {
        return of(rule.ruleCode(), rule.businessType(), rule.validGrade().getText(), rule.desc(),
                rule.seq(), rule.required(), rule.enable(), ruleClass.getName(), rule.validClass().getName());
    }

    public static RuleInfo of(String ruleCode, String businessType, String grade, String desc,
                              Integer seq, boolean required, boolean enable,
                              String ruleClassName, String validClassName) {
        return new RuleInfo(ruleCode, businessType, grade, desc, seq, required, enable, ruleClassName, validClassName);
    }

    public static RuleInfo of(String ruleCode, String businessType, String grade, String desc,
                              Integer seq, boolean required, boolean enable,
                              String ruleClassName, Class<?> validClass) {
        return new RuleInfo(ruleCode, businessType, grade, desc, seq, required, enable, ruleClassName, validClass);
    }

    public RuleInfo() {
    }

    private RuleInfo(String ruleCode, String businessType, String grade, String desc,
                     Integer seq, boolean required, boolean enable,
                     String ruleClassName, Class<?> validClass) {
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.desc = desc;
        this.seq = seq;
        this.required = required;
        this.enable = enable;
        this.ruleClassName = ruleClassName;
        this.validClassName = validClass.getName();
        this.validClass = validClass;
    }

    private RuleInfo(String ruleCode, String businessType, String grade, String desc,
                     Integer seq, boolean required, boolean enable,
                     String ruleClassName, String validClassName) {
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.desc = desc;
        this.seq = seq;
        this.required = required;
        this.enable = enable;
        this.ruleClassName = ruleClassName;
        this.validClassName = validClassName;
        try {
            this.validClass = this.getClass().getClassLoader().loadClass(validClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getGrade() {
        return grade;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getSeq() {
        return seq;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getRuleClassName() {
        return ruleClassName;
    }

    public String getValidClassName() {
        return validClassName;
    }

    public Class<?> getValidClass() {
        return validClass;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setValidClass(Class<?> validClass) {
        this.validClass = validClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleInfo ruleInfo = (RuleInfo) o;
        return Objects.equals(ruleCode, ruleInfo.ruleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleCode);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
