package com.ylzinfo.ruler.domain;

import java.math.BigDecimal;

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
     * 获取校验字段的key
     *
     * @return 节点名
     */
    public String fieldKey() {
        String simpleName = validClassName.substring(validClassName.lastIndexOf('.') + 1);
        StringBuilder sb = new StringBuilder();
        char headCharacter = Character.toLowerCase(simpleName.charAt(0));
        String nodeName = sb.append(headCharacter).append(simpleName.substring(1)).toString();
        return nodeName + "." + fieldName;
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

    @Override
    public String toString() {
        return "ValidInfo{" +
                "id='" + id + '\'' +
                ", businessType='" + businessType + '\'' +
                ", validType='" + validType + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                ", required=" + required +
                ", enable=" + enable +
                ", validClassName='" + validClassName + '\'' +
                '}';
    }
}
