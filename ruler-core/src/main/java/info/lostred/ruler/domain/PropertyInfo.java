package info.lostred.ruler.domain;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * 属性信息
 *
 * @author lostred
 */
public class PropertyInfo implements Serializable {
    private Class<?> validClass;
    private Class<?> propertyType;
    private String propertyName;
    private boolean entityProperty;

    public static PropertyInfo of(PropertyDescriptor propertyDescriptor) {
        return new PropertyInfo(propertyDescriptor.getReadMethod().getDeclaringClass(),
                propertyDescriptor.getReadMethod().getReturnType(),
                propertyDescriptor.getName());
    }

    public PropertyInfo() {
    }

    private PropertyInfo(Class<?> validClass, Class<?> propertyType, String propertyName) {
        this.validClass = validClass;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.entityProperty = isEntity(propertyType);
    }

    /**
     * 是否是实体类类型
     *
     * @param _class 类的类对象
     * @return 是返回true，否则返回false
     */
    public static boolean isEntity(Class<?> _class) {
        return !String.class.equals(_class)
                && !Temporal.class.isAssignableFrom(_class)
                && !Date.class.isAssignableFrom(_class)
                && !Number.class.isAssignableFrom(_class);
    }

    public Class<?> getValidClass() {
        return validClass;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isEntityProperty() {
        return entityProperty;
    }

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "validClass=" + validClass +
                ", propertyType=" + propertyType +
                ", propertyName='" + propertyName + '\'' +
                ", entityProperty=" + entityProperty +
                '}';
    }
}
