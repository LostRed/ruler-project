package com.ylzinfo.ruler.configure;

import com.ylzinfo.ruler.autoconfigure.RulerProperties;
import com.ylzinfo.ruler.constants.RulerConstants;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.factory.DatabaseRuleFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import com.ylzinfo.ruler.jdbc.RulerDateSource;
import com.ylzinfo.ruler.utils.JdbcUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库初始化配置
 *
 * @author dengluwei
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(RulerProperties.class)
public class DatabaseInitConfiguration {
    private final RulerProperties rulerProperties;

    public DatabaseInitConfiguration(RulerProperties rulerProperties) {
        this.rulerProperties = rulerProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ruler.db-config", name = {"url", "username", "password"})
    public DataSource rulerDataSource(RulerProperties rulerProperties) {
        String driverClassName = rulerProperties.getDbConfig().getDriverClassName();
        String url = rulerProperties.getDbConfig().getUrl();
        String username = rulerProperties.getDbConfig().getUsername();
        String password = rulerProperties.getDbConfig().getPassword();
        return new RulerDateSource(driverClassName, url, username, password);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ruler.valid-config", name = "init-type", havingValue = "db")
    public ValidConfiguration defaultValidConfiguration(DataSource dataSource) {
        String validInfoTableName = rulerProperties.getValidConfig().getTableName();
        String createTableSql = JdbcUtils.parseSql(RulerConstants.CREATE_VALID_INFO_SQL,
                RulerConstants.ORIGIN_VALID_INFO_TABLE_NAME, validInfoTableName);
        JdbcUtils.execute(dataSource, createTableSql);
        String selectSql = JdbcUtils.parseSql(RulerConstants.SELECT_VALID_INFO_SQL);
        List<ValidInfo> validInfos = JdbcUtils.query(dataSource, selectSql, ValidInfo.class);
        return new ValidConfiguration(validInfos);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ruler.rule-config", name = "init-type", havingValue = "db")
    public RuleFactory dbRuleFactory(ValidConfiguration validConfiguration, DataSource dataSource) {
        return new DatabaseRuleFactory(validConfiguration, dataSource, rulerProperties.getRuleConfig().getTableName());
    }
}
