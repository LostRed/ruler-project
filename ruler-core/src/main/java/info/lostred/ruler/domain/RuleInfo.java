package info.lostred.ruler.domain;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constant.Grade;

import java.util.List;
import java.util.Objects;

/**
 * 规则信息
 *
 * @author lostred
 */
public class RuleInfo {
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
    private String desc;
    /**
     * 规则执行的顺序号
     */
    private Integer seq;
    /**
     * 规则类型
     */
    private Class<?> ruleClass;
    /**
     * 校验信息
     */
    private List<ValidInfo> validInfos;

    public static RuleInfo of(Rule rule) {
        return new RuleInfo(rule.ruleCode(), rule.businessType(), rule.grade(), rule.desc(), rule.seq(), null);
    }

    public static RuleInfo of(String ruleCode, String businessType, Grade grade, String desc, Integer seq, List<ValidInfo> validInfos) {
        return new RuleInfo(ruleCode, businessType, grade, desc, seq, validInfos);
    }

    public RuleInfo() {
    }

    public RuleInfo(String ruleCode, String businessType, Grade grade, String desc, Integer seq, List<ValidInfo> validInfos) {
        this.ruleCode = ruleCode;
        this.businessType = businessType;
        this.grade = grade;
        this.desc = desc;
        this.seq = seq;
        this.validInfos = validInfos;
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

    public Class<?> getRuleClass() {
        return ruleClass;
    }

    public void setRuleClass(Class<?> ruleClass) {
        this.ruleClass = ruleClass;
    }

    public List<ValidInfo> getValidInfos() {
        return validInfos;
    }

    public void setValidInfos(List<ValidInfo> validInfos) {
        this.validInfos = validInfos;
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
}
