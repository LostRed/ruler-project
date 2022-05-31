package info.lostred.ruler.domain;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * 属性信息
 * <p>规则引擎入参的字段属性信息</p>
 *
 * @author lostred
 */
public class PropertyInfo implements Serializable {
    private Class<?> domainClass;
    private Class<?> propertyType;
    private String propertyName;
    /**
     * 属性是否是内嵌类型
     * <p>当属性不是String, Temporal, Date, Number类型时，值为true</p>
     * 参考本类方法{@link PropertyInfo#isNested(Class)}
     */
    private boolean nested;

    public static PropertyInfo of(PropertyDescriptor propertyDescriptor) {
        return new PropertyInfo(propertyDescriptor.getReadMethod().getDeclaringClass(),
                propertyDescriptor.getReadMethod().getReturnType(),
                propertyDescriptor.getName());
    }

    public PropertyInfo() {
    }

    private PropertyInfo(Class<?> domainClass, Class<?> propertyType, String propertyName) {
        this.domainClass = domainClass;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.nested = isNested(propertyType);
    }

    /**
     * 是否是内嵌类型
     *
     * @param _class 类的类对象
     * @return 是返回true，否则返回false
     */
    public static boolean isNested(Class<?> _class) {
        return !String.class.equals(_class)
                && !Temporal.class.isAssignableFrom(_class)
                && !Date.class.isAssignableFrom(_class)
                && !Number.class.isAssignableFrom(_class);
    }

    public Class<?> getDomainClass() {
        return domainClass;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isNested() {
        return nested;
    }
}
