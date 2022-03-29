package com.ylzinfo.ruler.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 校验信息
 *
 * @author dengluwei
 */
public class ValidInfo {
    /**
     * id
     */
    private String id;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 校验类型
     */
    private String validType;
    /**
     * 校验字段名
     */
    private String fieldName;
    /**
     * 下限值
     */
    private BigDecimal lowerLimit;
    /**
     * 上限值
     */
    private BigDecimal upperLimit;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 是否强制使用
     */
    private boolean required;
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 校验类型的全限定类名
     */
    private String validClassName;
    /**
     * 规则约束的类
     */
    private Class<?> validClass;

    public ValidInfo() {
    }

    public ValidInfo(String id, String businessType, String validType, String fieldName, String validClassName) {
        this.id = id;
        this.businessType = businessType;
        this.validType = validType;
        this.fieldName = fieldName;
        this.required = true;
        this.enable = true;
        this.validClassName = validClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidInfo validInfo = (ValidInfo) o;
        return Objects.equals(id, validInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
