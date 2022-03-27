package com.ylzinfo.ruler.configure;

import com.ylzinfo.ruler.autoconfigure.RulerProperties;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.factory.DbRuleFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import com.ylzinfo.ruler.jdbc.JdbcUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DataSource.class, JdbcTemplate.class})
@ConditionalOnProperty(prefix = "ruler", name = "init-type", havingValue = "db")
@EnableConfigurationProperties(RulerProperties.class)
public class DbInitConfiguration {
    private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";
    private final RulerProperties rulerProperties;

    public DbInitConfiguration(RulerProperties rulerProperties) {
        this.rulerProperties = rulerProperties;
    }

    @Bean
    @ConditionalOnMissingBean
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
    public RuleFactory dbRuleFactory(ValidConfiguration validConfiguration, JdbcTemplate jdbcTemplate) {
        return new DbRuleFactory(validConfiguration, jdbcTemplate, rulerProperties);
    }
}
