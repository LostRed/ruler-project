package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.annotation.DomainScan;
import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.engine.*;
import info.lostred.ruler.factory.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
public class RulerAutoConfiguration {
    @Bean
    public String[] domainScanPackages(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DomainScan.class);
        return RulerAutoConfiguration.getConfigClasses(beans).stream()
                .flatMap(e -> Arrays.stream(e.getAnnotation(DomainScan.class).value()))
                .toArray(String[]::new);
    }

    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(String[] domainScanPackages) {
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
        public String[] ruleScanPackages(ApplicationContext applicationContext) {
            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RuleScan.class);
            return RulerAutoConfiguration.getConfigClasses(beans).stream()
                    .flatMap(e -> Arrays.stream(e.getAnnotation(RuleScan.class).value()))
                    .toArray(String[]::new);
        }

        @Bean
        @ConditionalOnMissingBean
        public RuleFactory ruleFactory(ExpressionParser parser, String[] ruleScanPackages) {
            return new DefaultRuleFactory(parser, ruleScanPackages);
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
        public RulesEngineFactory engineFactory(Collection<AbstractRulesEngine> abstractRulesEngines) {
            return new DefaultRulesEngineFactory(abstractRulesEngines);
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

    public static Set<Class<?>> getConfigClasses(Map<String, Object> beans) {
        return beans.values().stream()
                .map(bean -> {
                    String className = bean.getClass().getName();
                    if (className.contains("$$")) {
                        className = className.substring(0, className.indexOf("$$"));
                    }
                    try {
                        return RulerAutoConfiguration.class.getClassLoader().loadClass(className);
                    } catch (ClassNotFoundException ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
