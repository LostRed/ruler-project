package info.lostred.ruler.configure;

import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.autoconfigure.RulerProperties;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.factory.AnnotationRuleFactory;
import info.lostred.ruler.factory.RuleFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 * 注解初始化配置
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RulerProperties.class)
@ConditionalOnProperty(prefix = "ruler.rule-config", name = "init-type", havingValue = "annotation", matchIfMissing = true)
public class AnnotationInitializationConfiguration {
    private final ApplicationContext applicationContext;
    private final RulerProperties rulerProperties;

    public AnnotationInitializationConfiguration(ApplicationContext applicationContext, RulerProperties rulerProperties) {
        this.applicationContext = applicationContext;
        this.rulerProperties = rulerProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ValidConfiguration validConfiguration() {
        return new ValidConfiguration(null);
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleFactory annotationRuleFactory(ValidConfiguration rulesEngineConfiguration) {
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
            } catch (ClassNotFoundException ignored) {
            }
        }
        String[] scanBasePackages = rulerProperties.getRuleConfig().getScanBasePackages();
        if (scanBasePackages != null) {
            return new AnnotationRuleFactory(rulesEngineConfiguration, configClass, scanBasePackages);
        }
        return new AnnotationRuleFactory(rulesEngineConfiguration, configClass);
    }
}
