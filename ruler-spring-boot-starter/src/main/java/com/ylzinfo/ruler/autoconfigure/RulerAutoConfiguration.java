package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.constants.RulesEngineType;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.engine.CompleteRulesEngine;
import com.ylzinfo.ruler.engine.IncompleteRulesEngine;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
import com.ylzinfo.ruler.factory.ContextRuleFactory;
import com.ylzinfo.ruler.factory.DefaultRulesEngineFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import com.ylzinfo.ruler.jdbc.JdbcUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ruler自动配置类
 *
 * @author dengluwei
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "ruler", name = {"default-business-type", "default-valid-class"})
public class RulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RulesEngineFactory rulesEngineFactory(Collection<RulesEngine<?>> rulesEngines) {
        return new DefaultRulesEngineFactory(rulesEngines);
    }

    /**
     * 规则自动配置类
     */
    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
    @EnableConfigurationProperties(RulerProperties.class)
    public static class RuleAutoConfiguration {
        private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";

        private final ApplicationContext applicationContext;

        private final RulerProperties rulerProperties;

        public RuleAutoConfiguration(ApplicationContext applicationContext, RulerProperties rulerProperties) {
            this.applicationContext = applicationContext;
            this.rulerProperties = rulerProperties;
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
        @ConditionalOnProperty(prefix = "ruler.valid-config", name = "init-from-db", havingValue = "true")
        public ValidConfiguration defaultValidConfiguration(JdbcTemplate jdbcTemplate) {
            String validInfoTableName = rulerProperties.getValidConfig().getTableName();
            String defaultBusinessType = rulerProperties.getDefaultBusinessType();
            jdbcTemplate.execute(JdbcUtils.parseSql("create-valid-info", VALID_INFO_TABLE_NAME, validInfoTableName));
            String sql = JdbcUtils.parseSql("select-valid-info", VALID_INFO_TABLE_NAME, validInfoTableName);
            List<ValidInfo> validInfos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ValidInfo.class), defaultBusinessType);
            return new ValidConfiguration(validInfos);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
        @ConditionalOnProperty(prefix = "ruler.rule-config", name = "init-from-db", havingValue = "true")
        public RuleFactory databaseRuleFactory(ValidConfiguration validConfiguration, JdbcTemplate jdbcTemplate) {
            return new DatabaseRuleFactory(validConfiguration, jdbcTemplate, rulerProperties);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "ruler.rule-config", name = "init-from-db", havingValue = "false", matchIfMissing = true)
        public RuleFactory contextRuleFactory(ValidConfiguration validConfiguration) {
            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RuleScan.class);
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
                    configClass = this.getClass().getClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String[] scanBasePackages = rulerProperties.getRuleConfig().getScanBasePackages();
            if (scanBasePackages != null) {
                return new ContextRuleFactory(validConfiguration, configClass, scanBasePackages);
            }
            return new ContextRuleFactory(validConfiguration, configClass);
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
}
