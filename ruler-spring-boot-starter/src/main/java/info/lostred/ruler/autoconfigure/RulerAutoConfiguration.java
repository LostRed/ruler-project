package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
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
    public DomainFactory domainFactory(DefaultListableBeanFactory defaultListableBeanFactory,
                                       RulerProperties rulerProperties) {
        Stream<String> stream = classWithAnnotation(defaultListableBeanFactory, DomainScan.class).stream()
                .flatMap(e -> Arrays.stream(e.getAnnotation(DomainScan.class).value()));
        if (rulerProperties.getDomainScanPackages() == null) {
            return new DomainFactory(stream.toArray(String[]::new));
        }
        String[] domainScanPackages = Stream.concat(stream, Arrays.stream(rulerProperties.getDomainScanPackages()))
                .distinct()
                .toArray(String[]::new);
        return new DomainFactory(domainScanPackages);
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
    public static class RuleAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RuleFactory ruleFactory(DefaultListableBeanFactory defaultListableBeanFactory,
                                       ExpressionParser expressionParser,
                                       BeanResolver beanResolver,
                                       RulerProperties rulerProperties) {
            Stream<String> stream = classWithAnnotation(defaultListableBeanFactory, RuleScan.class).stream()
                    .flatMap(e -> Arrays.stream(e.getAnnotation(RuleScan.class).value()));
            if (rulerProperties.getRuleScanPackages() == null) {
                return new DefaultRuleFactory(expressionParser, beanResolver, stream.toArray(String[]::new));
            }
            String[] ruleScanPackages = Stream.concat(stream, Arrays.stream(rulerProperties.getRuleScanPackages()))
                    .distinct()
                    .toArray(String[]::new);
            return new DefaultRuleFactory(expressionParser, beanResolver, ruleScanPackages);
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
                return RulesEngineFactory.builder(CompleteRulesEngine.class)
                        .businessType(businessType)
                        .ruleFactory(ruleFactory)
                        .beanResolver(beanResolver)
                        .globalFunctions(globalFunctions)
                        .build();
            } else {
                return RulesEngineFactory.builder(IncompleteRulesEngine.class)
                        .businessType(businessType)
                        .ruleFactory(ruleFactory)
                        .beanResolver(beanResolver)
                        .globalFunctions(globalFunctions)
                        .build();
            }
        }
    }

    /**
     * 根据指定的注解类在bean工厂中获取标有改注解的类对象集合
     *
     * @param defaultListableBeanFactory bean工厂
     * @param annotationClass            注解类
     * @return 符合条件的类对象集合
     */
    private static Set<Class<?>> classWithAnnotation(DefaultListableBeanFactory defaultListableBeanFactory,
                                                     Class<? extends Annotation> annotationClass) {
        String[] beanNames = defaultListableBeanFactory.getBeanNamesForAnnotation(annotationClass);
        return Arrays.stream(beanNames)
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
                .collect(Collectors.toSet());
    }
}
