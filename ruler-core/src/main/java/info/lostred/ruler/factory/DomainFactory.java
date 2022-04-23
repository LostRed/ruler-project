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
    private final Set<Class<?>> classSet = new HashSet<>();
    private final Map<String, List<PropertyInfo>> propertyInfoMap = new HashMap<>();

    public DomainFactory(String... scanPackages) {
        if (scanPackages != null) {
            this.registerFromPackages(scanPackages);
        }
    }

    /**
     * 从包中注册校验类信息
     *
     * @param packages 包名数组
     */
    private void registerFromPackages(String[] packages) {
        for (String packageName : packages) {
            PackageScanUtils.getClasses(packageName).stream()
                    .peek(this.classSet::add)
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
        return this.classSet;
    }
}
