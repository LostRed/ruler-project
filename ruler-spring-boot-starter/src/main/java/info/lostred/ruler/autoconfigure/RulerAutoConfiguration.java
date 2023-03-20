package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.builder.RulesEngineBuilder;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    public DomainFactory domainFactory(ConfigurableListableBeanFactory beanFactory,
                                       RulerProperties rulerProperties) {
        String[] beanNames = beanFactory.getBeanNamesForAnnotation(DomainScan.class);
        Stream<String> packageStream = Arrays.stream(beanNames)
                .map(beanFactory::getBeanDefinition)
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

    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    public static class RuleAutoConfiguration {
        @Bean
        public RuleFactory ruleFactory(ConfigurableListableBeanFactory beanFactory) {
            List<RuleDefinition> ruleDefinitions = Arrays.stream(beanFactory.getBeanDefinitionNames())
                    .map(beanFactory::getBeanDefinition)
                    .filter(e -> e instanceof AnnotatedBeanDefinition)
                    .map(AnnotatedBeanDefinition.class::cast)
                    .map(AnnotatedBeanDefinition::getMetadata)
                    .filter(e -> e.hasAnnotation(Rule.class.getName()))
                    .map(e -> {
                        try {
                            return Class.forName(e.getClassName());
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .map(e -> RuleDefinitionBuilder.build(e).getRuleDefinition())
                    .collect(Collectors.toList());
            return new SpringRuleFactory(ruleDefinitions);
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
