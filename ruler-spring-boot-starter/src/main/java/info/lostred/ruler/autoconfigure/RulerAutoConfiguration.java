package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RulesEngineBuilder;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.*;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RulerProperties.class)
public class RulerAutoConfiguration {
    private final static Logger logger = Logger.getLogger(RulerAutoConfiguration.class.getName());

    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(DefaultListableBeanFactory defaultListableBeanFactory,
                                       RulerProperties rulerProperties) {
        String[] beanNames = defaultListableBeanFactory.getBeanNamesForAnnotation(DomainScan.class);
        Stream<String> packageStream = Arrays.stream(beanNames)
                .map(defaultListableBeanFactory::getBeanDefinition)
                .map(ScannedGenericBeanDefinition.class::cast)
                .map(beanDefinition -> {
                    String className = beanDefinition.getMetadata().getClassName();
                    try {
                        return Thread.currentThread().getContextClassLoader().loadClass(className);
                    } catch (ClassNotFoundException ignored) {
                        logger.warning(className + " is not found.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(e -> Arrays.stream(e.getAnnotation(DomainScan.class).value()));
        String[] scanPackages = Stream.concat(Arrays.stream(rulerProperties.getDomainScanPackages()), packageStream)
                .filter(StringUtils::hasText)
                .distinct()
                .toArray(String[]::new);
        return new DomainFactory(scanPackages);
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanResolver beanResolver(BeanFactory beanFactory) {
        return new BeanFactoryResolver(beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExpressionParser ExpressionParser() {
        return new SpelExpressionParser();
    }

    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    public static class RuleAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
        private final static String RULE_SCAN_PACKAGES_KEY = "ruler.rule-scan-packages";
        private Environment environment;

        @Bean
        @ConditionalOnMissingBean
        public RuleFactory ruleFactory(ObjectProvider<AbstractRule> abstractRules) {
            return new SpringRuleFactory(abstractRules);
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            String[] basePackages = Arrays.stream(StringUtils.commaDelimitedListToStringArray(environment.getProperty(RULE_SCAN_PACKAGES_KEY)))
                    .filter(StringUtils::hasText)
                    .distinct()
                    .toArray(String[]::new);
            if (basePackages.length == 0) {
                return;
            }
            ClassPathRuleScanner classPathRuleScanner = new ClassPathRuleScanner(registry);
            classPathRuleScanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getAnnotationMetadata().hasAnnotation(Rule.class.getName()));
            classPathRuleScanner.scan(basePackages);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }

    /**
     * 规则引擎自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RuleAutoConfiguration.class)
    public static class RulesEngineAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RulesEngineFactory rulesEngineFactory(Collection<RulesEngine> rulesEngines) {
            return new DefaultRulesEngineFactory(rulesEngines);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty("ruler.engine-type")
        public RulesEngine rulesEngine(RuleFactory ruleFactory, BeanResolver beanResolver,
                                       List<Method> globalFunctions, RulerProperties rulerProperties) {
            String type = rulerProperties.getEngineType().toUpperCase();
            String businessType = rulerProperties.getBusinessType();
            if (EngineType.COMPLETE.equals(EngineType.valueOf(type))) {
                return RulesEngineBuilder.build(CompleteRulesEngine.class)
                        .businessType(businessType)
                        .ruleFactory(ruleFactory)
                        .beanResolver(beanResolver)
                        .globalFunctions(globalFunctions)
                        .getRulesEngine();
            } else {
                return RulesEngineBuilder.build(IncompleteRulesEngine.class)
                        .businessType(businessType)
                        .ruleFactory(ruleFactory)
                        .beanResolver(beanResolver)
                        .globalFunctions(globalFunctions)
                        .getRulesEngine();
            }
        }
    }
}
