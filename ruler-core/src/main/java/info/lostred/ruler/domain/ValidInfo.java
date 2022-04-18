package info.lostred.ruler.domain;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.constants.ValidType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * 校验信息
 *
 * @author lostred
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

    private Set<Object> dict;

    public static ValidInfo ofRequired(String fieldName, String validClassName) {
        return ofRequired(RulerConstants.DEFAULT_BUSINESS_TYPE, fieldName, validClassName);
    }

    public static ValidInfo ofRequired(String businessType, String fieldName, String validClassName) {
        return ofRequired(UUID.randomUUID().toString(), businessType, fieldName, validClassName);
    }

    public static ValidInfo ofRequired(String id, String businessType, String fieldName, String validClassName) {
        return new ValidInfo(id, businessType, ValidType.REQUIRED.name(), fieldName,
                null, null, null, null, validClassName);
    }

    public static ValidInfo ofDict(String fieldName, String validClassName) {
        return ofDict(RulerConstants.DEFAULT_BUSINESS_TYPE, fieldName, validClassName);
    }

    public static ValidInfo ofDict(String businessType, String fieldName, String validClassName) {
        return ofDict(UUID.randomUUID().toString(), businessType, fieldName, validClassName);
    }

    public static ValidInfo ofDict(String id, String businessType, String fieldName, String validClassName) {
        return new ValidInfo(id, businessType, ValidType.DICT.name(), fieldName,
                null, null, null, null, validClassName);
    }

    public static ValidInfo ofNumberScope(String fieldName, BigDecimal lowerLimit, BigDecimal upperLimit, String validClassName) {
        return ofNumberScope(RulerConstants.DEFAULT_BUSINESS_TYPE, fieldName, lowerLimit, upperLimit, validClassName);
    }

    public static ValidInfo ofNumberScope(String businessType, String fieldName, BigDecimal lowerLimit, BigDecimal upperLimit, String validClassName) {
        return ofNumberScope(UUID.randomUUID().toString(), businessType, fieldName, lowerLimit, upperLimit, validClassName);
    }

    public static ValidInfo ofNumberScope(String id, String businessType, String fieldName, BigDecimal lowerLimit, BigDecimal upperLimit, String validClassName) {
        return new ValidInfo(id, businessType, ValidType.NUMBER_SCOPE.name(), fieldName, lowerLimit, upperLimit, null, null, validClassName);
    }

    public static ValidInfo ofDateTimeScope(String fieldName, LocalDateTime beginTime, LocalDateTime endTime, String validClassName) {
        return ofDateTimeScope(RulerConstants.DEFAULT_BUSINESS_TYPE, fieldName, beginTime, endTime, validClassName);
    }

    public static ValidInfo ofDateTimeScope(String businessType, String fieldName, LocalDateTime beginTime, LocalDateTime endTime, String validClassName) {
        return ofDateTimeScope(UUID.randomUUID().toString(), businessType, fieldName, beginTime, endTime, validClassName);
    }

    public static ValidInfo ofDateTimeScope(String id, String businessType, String fieldName, LocalDateTime beginTime, LocalDateTime endTime, String validClassName) {
        return new ValidInfo(id, businessType, ValidType.DATETIME_SCOPE.name(), fieldName, null, null, beginTime, endTime, validClassName);
    }

    public ValidInfo() {
    }

    private ValidInfo(String id, String businessType, String validType, String fieldName,
                      BigDecimal lowerLimit, BigDecimal upperLimit, LocalDateTime beginTime, LocalDateTime endTime,
                      String validClassName) {
        this.id = id;
        this.businessType = businessType;
        this.validType = validType;
        this.fieldName = fieldName;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.required = true;
        this.enable = true;
        this.validClassName = validClassName;
        try {
            this.validClass = this.getClass().getClassLoader().loadClass(validClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getId() {
        return id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getValidType() {
        return validType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getValidClassName() {
        return validClassName;
    }

    public Class<?> getValidClass() {
        return validClass;
    }

    public Set<Object> getDict() {
        return dict;
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

    public void setDict(Set<Object> dict) {
        this.dict = dict;
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
