package info.lostred.ruler.domain;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.rule.SingleFieldRule;

import java.util.Objects;

/**
 * 规则信息
 *
 * @author lostred
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
        return of(rule.ruleCode(), rule.businessType(), rule.validGrade().name(), rule.desc(),
                rule.seq(), rule.required(), rule.enable(), ruleClass.getName(), rule.validClass());
    }

    public static RuleInfo of(String ruleCode, String businessType, String desc,
                              String ruleClassName, String validClassName) {
        return RuleInfo.of(ruleCode, businessType, ValidGrade.ILLEGAL.name(), desc,
                0, false, true, ruleClassName, validClassName);
    }

    public static RuleInfo of(String ruleCode, String businessType, String desc,
                              String ruleClassName, Class<?> validClass) {
        return RuleInfo.of(ruleCode, businessType, ValidGrade.ILLEGAL.name(), desc,
                0, false, true, ruleClassName, validClass);
    }

    public static RuleInfo of(String ruleCode, String businessType, String grade, String desc,
                              Integer seq, boolean required, boolean enable,
                              String ruleClassName, String validClassName) {
        RuleInfo ruleInfo = new RuleInfo(ruleCode, businessType, grade, desc,
                seq, required, enable, ruleClassName);
        ruleInfo.setValidClassName(validClassName);
        return ruleInfo;
    }

    public static RuleInfo of(String ruleCode, String businessType, String grade, String desc,
                              Integer seq, boolean required, boolean enable,
                              String ruleClassName, Class<?> validClass) {
        RuleInfo ruleInfo = new RuleInfo(ruleCode, businessType, grade, desc,
                seq, required, enable, ruleClassName);
        ruleInfo.setValidClass(validClass);
        return ruleInfo;
    }

    public RuleInfo() {
    }

    public RuleInfo(String ruleCode, String businessType, String grade, String desc,
                    Integer seq, boolean required, boolean enable, String ruleClassName) {
        try {
            Class<?> ruleClass = RuleInfo.class.getClassLoader().loadClass(ruleClassName);
            if (!SingleFieldRule.class.isAssignableFrom(ruleClass)
                    && RulerConstants.DEFAULT_BUSINESS_TYPE.equals(businessType)) {
                throw new IllegalArgumentException("BusinessType can not be " +
                        RulerConstants.DEFAULT_BUSINESS_TYPE + ".");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.desc = desc;
        this.seq = seq;
        this.required = required;
        this.enable = enable;
        this.ruleClassName = ruleClassName;
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

    public void setValidClassName(String validClassName) {
        this.validClassName = validClassName;
        try {
            this.validClass = this.getClass().getClassLoader().loadClass(validClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setValidClass(Class<?> validClass) {
        if (validClass == null) {
            throw new NullPointerException("The parameter 'validClass' can not be null.");
        }
        this.validClass = validClass;
        this.validClassName = validClass.getName();
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
