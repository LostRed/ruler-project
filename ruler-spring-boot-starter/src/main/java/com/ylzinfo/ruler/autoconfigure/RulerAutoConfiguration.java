package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.constants.RulesEngineType;
import com.ylzinfo.ruler.core.*;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.engine.CompleteRulesEngine;
import com.ylzinfo.ruler.engine.IncompleteRulesEngine;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
import com.ylzinfo.ruler.support.RuleFactory;
import com.ylzinfo.ruler.support.RulesEngineFactory;
import com.ylzinfo.ruler.support.TypeReference;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * ruler自动配置类
 *
 * @author dengluwei
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
@ConditionalOnProperty("ruler.default-business-type")
public class RulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RulesEngineManager rulesEngineManager(Collection<RulesEngine<?>> rulesEngines) {
        return new RulesEngineManagerImpl(rulesEngines);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
    @ConditionalOnSingleCandidate(DataSource.class)
    @EnableConfigurationProperties(RulerProperties.class)
    @ConditionalOnProperty("ruler.default-valid-class")
    static class RuleAutoConfiguration<E> {

        private final RulerProperties<E> rulerProperties;

        public RuleAutoConfiguration(RulerProperties<E> rulerProperties) {
            this.rulerProperties = rulerProperties;
        }

        @Bean
        @ConditionalOnBean(name = "jdbcTemplate")
        @ConditionalOnProperty(prefix = "ruler", name = "config-table-init", havingValue = "true", matchIfMissing = true)
        public RulerConfigTableInitializer rulerConfigTableInitializer(JdbcTemplate jdbcTemplate) {
            return new RulerConfigTableInitializer(jdbcTemplate);
        }

        @Bean
        @ConditionalOnMissingBean
        public ValidConfiguration defaultValidConfiguration(RulerConfigTableInitializer initializer) {
            ValidConfiguration validConfiguration = new ValidConfiguration();
            List<ValidInfo> validInfos = initializer.fetchValidInfo(rulerProperties);
            validConfiguration.addValidInfo(validInfos);
            return validConfiguration;
        }

        @Bean
        @ConditionalOnMissingBean
        public List<Rule<E>> defaultRules(RulerConfigTableInitializer initializer, ValidConfiguration validConfiguration) {
            List<RuleInfo> ruleInfos = initializer.fetchRuleInfo(rulerProperties);
            return RuleFactory.rulesBuilder(validConfiguration, ruleInfos, rulerProperties.getDefaultValidClass()).build();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(RuleAutoConfiguration.class)
    @EnableConfigurationProperties(RulerProperties.class)
    static class RulesEngineAutoConfiguration<E> {

        private final RulerProperties<E> rulerProperties;

        public RulesEngineAutoConfiguration(RulerProperties<E> rulerProperties) {
            this.rulerProperties = rulerProperties;
        }

        @Bean
        @ConditionalOnMissingBean
        public RulesEngine<E> defaultRulesEngine(List<Rule<E>> rules) {
            String type = rulerProperties.getDefaultRulesEngineType().toUpperCase();
            if (RulesEngineType.COMPLETE.equals(RulesEngineType.valueOf(type))) {
                TypeReference<CompleteRulesEngine<E>> typeReference = new TypeReference<CompleteRulesEngine<E>>() {
                };
                return RulesEngineFactory.builder(typeReference, rules).build();
            } else if (RulesEngineType.INCOMPLETE.equals(RulesEngineType.valueOf(type))) {
                TypeReference<IncompleteRulesEngine<E>> typeReference = new TypeReference<IncompleteRulesEngine<E>>() {
                };
                return RulesEngineFactory.builder(typeReference, rules).build();
            } else {
                TypeReference<SimpleRulesEngine<E>> typeReference = new TypeReference<SimpleRulesEngine<E>>() {
                };
                return RulesEngineFactory.builder(typeReference, rules).build();
            }
        }
    }
}
