package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.configure.RuleInitializationConfiguration;
import info.lostred.ruler.constant.EngineType;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.engine.SimpleRulesEngine;
import info.lostred.ruler.factory.DefaultRulesEngineFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.factory.RulesEngineFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Collection;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "ruler", name = "default-valid-class")
public class RulerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BeanResolver beanResolver(ApplicationContext applicationContext) {
        return new BeanFactoryResolver(applicationContext);
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
    @Import(RuleInitializationConfiguration.class)
    public static class RuleAutoConfiguration {
    }

    /**
     * 规则引擎自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RuleAutoConfiguration.class)
    @EnableConfigurationProperties(RulerProperties.class)
    public static class RulesEngineAutoConfiguration {
        private final RulerProperties rulerProperties;

        public RulesEngineAutoConfiguration(RulerProperties rulerProperties) {
            this.rulerProperties = rulerProperties;
        }

        @Bean
        @ConditionalOnMissingBean
        public RulesEngineFactory defaultRulesEngineFactory(Collection<RulesEngine> rulesEngines) {
            return new DefaultRulesEngineFactory(rulesEngines);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty("ruler.rules-engine-config.type")
        public RulesEngine defaultRulesEngine(RuleFactory ruleFactory,
                                              BeanResolver beanResolver,
                                              ExpressionParser parser) {
            String type = rulerProperties.getRulesEngineConfig().getType().toUpperCase();
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
}
