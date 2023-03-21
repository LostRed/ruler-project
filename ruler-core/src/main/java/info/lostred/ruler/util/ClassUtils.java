package info.lostred.ruler.util;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 路径径匹配类扫描工具
 *
 * @author lostred
 */
public final class ClassUtils {
    private final static Environment ENVIRONMENT = new StandardEnvironment();
    private final static ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private final static MetadataReaderFactory METADATA_READER_FACTORY = new SimpleMetadataReaderFactory();

    /**
     * 找到包扫描路径下的所有类对象集合
     *
     * @param basePackage 包名
     * @return 类对象集合
     */
    public static Set<Class<?>> getTypeFromPackage(String basePackage) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                org.springframework.util.ClassUtils.convertClassNameToResourcePath(ENVIRONMENT.resolveRequiredPlaceholders(basePackage)) +
                "/**/*.class";
        Set<Class<?>> set = new HashSet<>();
        try {
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(packageSearchPath);
            for (Resource resource : resources) {
                ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                String className = classMetadata.getClassName();
                set.add(loadClass(className));
            }
            return set;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载类
     *
     * @param className 全限定类名
     * @return 类对象
     */
    public static Class<?> loadClass(String className) {
        ClassLoader classLoader = ClassUtils.class.getClassLoader();
        try {
            return org.springframework.util.ClassUtils.forName(className, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
