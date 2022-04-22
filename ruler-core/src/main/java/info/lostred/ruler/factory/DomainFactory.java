package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.domain.PropertyInfo;
import info.lostred.ruler.util.PackageScanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 领域模型工厂
 *
 * @author lostred
 */
public class DomainFactory {
    private final Map<String, List<PropertyInfo>> propertyInfoMap = new ConcurrentHashMap<>();
    private final Class<?> configClass;
    private final String[] anotherPackages;

    public DomainFactory(Class<?> configClass, String... anotherPackages) {
        this.configClass = configClass;
        this.anotherPackages = anotherPackages;
        this.init();
    }

    public void init() {
        String[] mergedPackages;
        Stream<String> stream = Arrays.stream(this.anotherPackages);
        if (this.configClass != null && this.configClass.isAnnotationPresent(DomainScan.class)) {
            String[] scanBasePackages = this.configClass.getAnnotation(DomainScan.class).value();
            mergedPackages = Stream.concat(stream, Stream.of(scanBasePackages))
                    .distinct()
                    .toArray(String[]::new);
        } else {
            mergedPackages = stream.distinct().toArray(String[]::new);
        }
        this.registerFromPackages(mergedPackages);
    }

    /**
     * 从包中注册校验类信息
     *
     * @param packages 包名数组
     */
    private void registerFromPackages(String[] packages) {
        for (String packageName : packages) {
            try {
                Set<String> classNames = PackageScanUtils.findClassNames(packageName);
                classNames.stream()
                        .map(PackageScanUtils::loadClass)
                        .filter(Objects::nonNull)
                        .forEach(e -> this.propertyInfoMap.put(e.getName(), this.getPropertyList(e)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取属性信息集合
     *
     * @param domainClass 领域模型类的类对象
     * @return 属性信息集合
     */
    private List<PropertyInfo> getPropertyList(Class<?> domainClass) {
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
     * 注册领域模型类
     *
     * @param domainClass 领域模型类的类对象
     */
    public void registerDomain(Class<?> domainClass) {
        this.propertyInfoMap.put(domainClass.getName(), this.getPropertyList(domainClass));
    }
}
