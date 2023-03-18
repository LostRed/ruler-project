package info.lostred.ruler.factory;

import info.lostred.ruler.domain.PropertyInfo;
import info.lostred.ruler.util.ClassPathScanUtils;

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
    /**
     * 规则引擎入参类型集合
     */
    private final Set<Class<?>> domainSet = new HashSet<>();
    /**
     * 规则引擎入参类型的字段信息地图
     * <p>key为全限定类名，值为属性信息集合</p>
     */
    private final Map<String, List<PropertyInfo>> propertyInfoMap = new HashMap<>();

    public DomainFactory(String... scanPackages) {
        this.registerDomains(scanPackages);
    }

    /**
     * 从包中注册校验类信息
     */
    private void registerDomains(String... scanPackages) {
        if (scanPackages == null || scanPackages.length == 0) {
            throw new IllegalArgumentException("At least one base package must be specified");
        }
        for (String packageName : scanPackages) {
            ClassPathScanUtils.getClasses(packageName).stream()
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

    /**
     * 注册领域类型
     *
     * @param domainClass 领域类对象
     */
    public void registerDomain(Class<?> domainClass) {
        this.domainSet.add(domainClass);
        this.propertyInfoMap.put(domainClass.getName(), this.getPropertyList(domainClass));
    }
}
