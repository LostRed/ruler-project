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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
public class RulerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(ApplicationContext applicationContext,
                                       RulerProperties rulerProperties) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DomainScan.class);
        Class<?> configClass = getConfigClass(beans);
        String[] scanBasePackages = rulerProperties.getDomainDefaultScope();
        if (scanBasePackages != null) {
            return new DomainFactory(configClass, scanBasePackages);
        }
        return new DomainFactory(configClass);
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
        public RuleFactory ruleFactory(ApplicationContext applicationContext,
                                       ExpressionParser parser,
                                       RulerProperties rulerProperties) {
            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RuleScan.class);
            Class<?> configClass = RulerAutoConfiguration.getConfigClass(beans);
            String[] scanBasePackages = rulerProperties.getRuleDefaultScope();
            if (scanBasePackages != null) {
                return new DefaultRuleFactory(parser, configClass, scanBasePackages);
            }
            return new DefaultRuleFactory(parser, configClass);
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

    public static Class<?> getConfigClass(Map<String, Object> beans) {
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
                configClass = RulerAutoConfiguration.class.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return configClass;
    }
}
