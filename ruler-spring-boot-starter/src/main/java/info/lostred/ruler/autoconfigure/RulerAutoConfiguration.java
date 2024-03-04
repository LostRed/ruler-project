package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.builder.RuleFactoryBuilder;
import info.lostred.ruler.builder.RulesEngineBuilder;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.*;
import info.lostred.ruler.util.ClassUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(ConfigurableListableBeanFactory beanFactory,
                                       RulerProperties rulerProperties) {
        Stream<String> packageStream = Arrays.stream(beanFactory.getBeanNamesForAnnotation(DomainScan.class))
                .map(beanFactory::getBeanDefinition)
                .map(AnnotatedBeanDefinition.class::cast)
                .map(AnnotatedBeanDefinition::getMetadata)
                .map(e -> e.getAnnotationAttributes(DomainScan.class.getName()))
                .map(AnnotationAttributes::fromMap)
                .filter(Objects::nonNull)
                .map(e -> e.getStringArray("value"))
                .flatMap(Arrays::stream);
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
        @ConditionalOnMissingBean
        public ExpressionParser expressionParser() {
            return new SpelExpressionParser();
        }

        @Bean
        @ConditionalOnMissingBean
        public RuleFactory ruleFactory(ConfigurableListableBeanFactory beanFactory) {
            List<RuleDefinition> ruleDefinitions = Arrays.stream(beanFactory.getBeanDefinitionNames())
                    .map(beanFactory::getBeanDefinition)
                    .filter(e -> e instanceof AnnotatedBeanDefinition)
                    .map(AnnotatedBeanDefinition.class::cast)
                    .map(AnnotatedBeanDefinition::getMetadata)
                    .filter(e -> e.hasAnnotation(Rule.class.getName()))
                    .map(e -> ClassUtils.loadClass(e.getClassName()))
                    .map(e -> RuleDefinitionBuilder.build(e).getRuleDefinition())
                    .collect(Collectors.toList());
            RuleFactoryBuilder builder = RuleFactoryBuilder.build(SpringRuleFactory.class);
            return builder.registerRuleDefinition(ruleDefinitions)
                    .getRuleFactory();
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
