package info.lostred.ruler.autoconfigure;

import info.lostred.ruler.configure.AnnotationInitializationConfiguration;
import info.lostred.ruler.constant.RulesEngineType;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.IncompleteRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.DefaultRulesEngineFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.factory.RulesEngineFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;

/**
 * ruler自动配置类
 *
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "ruler", name = "default-valid-class")
public class RulerAutoConfiguration {
    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @Import(AnnotationInitializationConfiguration.class)
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
        public RulesEngineFactory defaultRulesEngineFactory(Collection<RulesEngine<?>> rulesEngines) {
            return new DefaultRulesEngineFactory(rulesEngines);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty("ruler.rules-engine-config.type")
        @SuppressWarnings("unchecked")
        public RulesEngine<?> defaultRulesEngine(RuleFactory ruleFactory) {
            String type = rulerProperties.getRulesEngineConfig().getType().toUpperCase();
            String defaultBusinessType = rulerProperties.getDefaultBusinessType();
            Class<?> defaultValidClass = rulerProperties.getDefaultValidClass();
            if (RulesEngineType.COMPLETE.equals(RulesEngineType.valueOf(type))) {
                return RulesEngineFactory.builder(ruleFactory, defaultBusinessType, CompleteRulesEngine.class, defaultValidClass).build();
            } else if (RulesEngineType.INCOMPLETE.equals(RulesEngineType.valueOf(type))) {
                return RulesEngineFactory.builder(ruleFactory, defaultBusinessType, IncompleteRulesEngine.class, defaultValidClass).build();
            } else {
                throw new RuntimeException("Unknown rules engine type: " + type);
            }
        }
    }
}
