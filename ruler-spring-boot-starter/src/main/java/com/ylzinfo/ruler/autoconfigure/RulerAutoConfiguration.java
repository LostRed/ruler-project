package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.configure.ContextInitConfiguration;
import com.ylzinfo.ruler.configure.DbInitConfiguration;
import com.ylzinfo.ruler.constants.RulesEngineType;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.engine.CompleteRulesEngine;
import com.ylzinfo.ruler.engine.IncompleteRulesEngine;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
import com.ylzinfo.ruler.factory.DefaultRulesEngineFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;

/**
 * ruler自动配置类
 *
 * @author dengluwei
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "ruler", name = {"default-business-type", "default-valid-class"})
public class RulerAutoConfiguration {
    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
    @Import({ContextInitConfiguration.class, DbInitConfiguration.class})
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
        public RulesEngineFactory rulesEngineFactory(Collection<RulesEngine<?>> rulesEngines) {
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
                return DefaultRulesEngineFactory.builder(ruleFactory, defaultBusinessType, CompleteRulesEngine.class, defaultValidClass).build();
            } else if (RulesEngineType.INCOMPLETE.equals(RulesEngineType.valueOf(type))) {
                return DefaultRulesEngineFactory.builder(ruleFactory, defaultBusinessType, IncompleteRulesEngine.class, defaultValidClass).build();
            } else {
                return DefaultRulesEngineFactory.builder(ruleFactory, defaultBusinessType, SimpleRulesEngine.class, defaultValidClass).build();
            }
        }
    }
}
