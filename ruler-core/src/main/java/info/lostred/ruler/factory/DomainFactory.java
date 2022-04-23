package info.lostred.ruler.factory;

import info.lostred.ruler.domain.PropertyInfo;
import info.lostred.ruler.util.PackageScanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域模型工厂
 *
 * @author lostred
 */
public class DomainFactory {
    private final Set<Class<?>> domainSet = new HashSet<>();
    private final Map<String, List<PropertyInfo>> propertyInfoMap = new HashMap<>();
    private final String[] scanPackages;

    public DomainFactory(String... scanPackages) {
        this.scanPackages = scanPackages;
        this.registerFromPackages();
    }

    /**
     * 从包中注册校验类信息
     */
    private void registerFromPackages() {
        if (scanPackages == null || scanPackages.length == 0) {
            throw new IllegalArgumentException("Have not to set the scan packages.");
        }
        for (String packageName : scanPackages) {
            PackageScanUtils.getClasses(packageName).stream()
                    .peek(this.domainSet::add)
                    .forEach(e -> this.propertyInfoMap.put(e.getName(), this.getPropertyList(e)));
        }
    }

    /**
     * 获取属性信息集合
     *
     * @param domainClass 领域模型类的类对象
     * @return 属性信息集合
     */
    public List<PropertyInfo> getPropertyList(Class<?> domainClass) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(domainClass, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            return Arrays.stream(propertyDescriptors).map(PropertyInfo::of).collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取校验类的属性信息集合
     *
     * @param className 全限定类名
     * @return 属性信息集合
     */
    public List<PropertyInfo> getPropertyList(String className) {
        return this.propertyInfoMap.get(className);
    }

    /**
     * 获取所有领域模型类的类对象集合
     *
     * @return 领域模型类的类对象集合
     */
    public Set<Class<?>> getAllDomain() {
        return this.domainSet;
    }
}
