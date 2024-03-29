package info.lostred.ruler.annotation;

import info.lostred.ruler.factory.RuleAnnotationPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 规则扫描注册
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleScannerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(RuleScan.class.getName()));
        assert mapperScanAttrs != null;
        String[] basePackages = Arrays.stream(mapperScanAttrs.getStringArray("value"))
                .filter(StringUtils::hasText)
                .distinct()
                .toArray(String[]::new);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(RuleAnnotationPostProcessor.class);
        builder.addConstructorArgValue(basePackages);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
