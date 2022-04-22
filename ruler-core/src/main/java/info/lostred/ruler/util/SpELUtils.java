package info.lostred.ruler.util;

import info.lostred.ruler.domain.PropertyInfo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpELUtils {
    public static String dictLabel(String dictType) {
        String dictMap = beanLabel("dictMap");
        return "@" + dictMap + ".get('" + dictType + "')";
    }

    public static String beanLabel(String beanName) {
        return "@" + beanName;
    }

    public static List<PropertyInfo> propertyList(Class<?> entityClass) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        return Arrays.stream(propertyDescriptors).map(PropertyInfo::of).collect(Collectors.toList());
    }
}
