package com.ylzinfo.ruler.configure;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.autoconfigure.RulerProperties;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.factory.AnnotationRuleFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RulerProperties.class)
public class AnnotationInitConfiguration {
    private final ApplicationContext applicationContext;
    private final RulerProperties rulerProperties;

    public AnnotationInitConfiguration(ApplicationContext applicationContext, RulerProperties rulerProperties) {
        this.applicationContext = applicationContext;
        this.rulerProperties = rulerProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ruler.valid-config", name = "init-type", havingValue = "annotation", matchIfMissing = true)
    public ValidConfiguration defaultValidConfiguration() {
        return new ValidConfiguration(null);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ruler.rule-config", name = "init-type", havingValue = "annotation", matchIfMissing = true)
    public RuleFactory annotationRuleFactory(ValidConfiguration validConfiguration) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RuleScan.class);
        Class<?> configClass = null;
        Optional<Object> first = beans.values().stream().findFirst();
        if (first.isPresent()) {
            Object object = first.get();
            String className = object.getClass().getName();
            int suffix = className.indexOf("$$");
            if (suffix != -1) {
                className = className.substring(0, className.indexOf("$$"));
            }
            try {
                configClass = this.getClass().getClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String[] scanBasePackages = rulerProperties.getRuleConfig().getScanBasePackages();
        if (scanBasePackages != null) {
            return new AnnotationRuleFactory(validConfiguration, configClass, scanBasePackages);
        }
        return new AnnotationRuleFactory(validConfiguration, configClass);
    }
}
