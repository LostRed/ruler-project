package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.engine.SimpleRulesEngine;
import info.lostred.ruler.factory.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
public class RulerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(DefaultListableBeanFactory defaultListableBeanFactory,
                                       RulerProperties rulerProperties) {
        Stream<String> stream = getConfigClasses(defaultListableBeanFactory, DomainScan.class).stream()
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
    public ExpressionParser parser() {
        return new SpelExpressionParser();
    }

    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(RulerProperties.class)
    public static class RuleAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RuleFactory ruleFactory(DefaultListableBeanFactory defaultListableBeanFactory,
                                       RulerProperties rulerProperties) {
            Stream<String> stream = getConfigClasses(defaultListableBeanFactory, RuleScan.class).stream()
                    .flatMap(e -> Arrays.stream(e.getAnnotation(RuleScan.class).value()));
            if (rulerProperties.getRuleScanPackages() == null) {
                return new DefaultRuleFactory(stream.toArray(String[]::new));
            }
            String[] ruleScanPackages = Stream.concat(stream, Arrays.stream(rulerProperties.getRuleScanPackages()))
                    .distinct()
                    .toArray(String[]::new);
            return new DefaultRuleFactory(ruleScanPackages);
        }
    }

    /**
     * 规则引擎自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RuleAutoConfiguration.class)
    @EnableConfigurationProperties(RulerProperties.class)
    public static class RulesEngineAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RulesEngineFactory rulesEngineFactory(Collection<RulesEngine> rulesEngines) {
            return new DefaultRulesEngineFactory(rulesEngines);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty("ruler.engine-type")
        public RulesEngine rulesEngine(RuleFactory ruleFactory,
                                       BeanResolver beanResolver,
                                       ExpressionParser parser,
                                       RulerProperties rulerProperties) {
            String type = rulerProperties.getEngineType().toUpperCase();
            String businessType = rulerProperties.getBusinessType();
            if (EngineType.COMPLETE.equals(EngineType.valueOf(type))) {
                return RulesEngineFactory.builder(ruleFactory,
                        businessType, beanResolver, parser,
                        CompleteRulesEngine.class).build();
            } else if (EngineType.INCOMPLETE.equals(EngineType.valueOf(type))) {
                return RulesEngineFactory.builder(ruleFactory,
                        businessType, beanResolver, parser,
                        IncompleteRulesEngine.class).build();
            } else {
                return RulesEngineFactory.builder(ruleFactory,
                        businessType, beanResolver, parser,
                        SimpleRulesEngine.class).build();
            }
        }
    }

    public static Set<Class<?>> getConfigClasses(DefaultListableBeanFactory defaultListableBeanFactory,
                                                 Class<? extends Annotation> annotationClass) {
        String[] beanNames = defaultListableBeanFactory.getBeanNamesForAnnotation(annotationClass);
        return Arrays.stream(beanNames)
                .map(defaultListableBeanFactory::getBeanDefinition)
                .map(beanDefinition -> {
                    String beanClassName = beanDefinition.getBeanClassName();
                    assert beanClassName != null;
                    if (beanClassName.contains("$$")) {
                        beanClassName = beanClassName.substring(0, beanClassName.indexOf("$$"));
                    }
                    try {
                        return RulerAutoConfiguration.class.getClassLoader().loadClass(beanClassName);
                    } catch (ClassNotFoundException ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
