package info.lostred.ruler.configure;

import info.lostred.ruler.autoconfigure.RulerProperties;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.factory.DatabaseRuleFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.jdbc.RulerDateSource;
import info.lostred.ruler.util.JdbcUtils;
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
 * @author lostred
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(RulerProperties.class)
@ConditionalOnProperty(prefix = "ruler.valid-config", name = "init-type", havingValue = "db")
public class DatabaseInitializationConfiguration {
    private final RulerProperties rulerProperties;

    public DatabaseInitializationConfiguration(RulerProperties rulerProperties) {
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
    public ValidConfiguration validConfiguration(DataSource dataSource) {
        String validInfoTableName = rulerProperties.getValidConfig().getTableName();
        String createTableSql = JdbcUtils.parseSql(RulerConstants.CREATE_VALID_INFO_SQL,
                RulerConstants.ORIGIN_VALID_INFO_TABLE_NAME, validInfoTableName);
        JdbcUtils.execute(dataSource, createTableSql);
        String selectSql = JdbcUtils.parseSql(RulerConstants.SELECT_VALID_INFO_SQL);
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        List<ValidInfo> validInfos = JdbcUtils.query(dataSource, selectSql, ValidInfo.class, defaultBusinessType);
        return new ValidConfiguration(validInfos);
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleFactory databaseRuleFactory(ValidConfiguration validConfiguration, DataSource dataSource) {
        return new DatabaseRuleFactory(validConfiguration, dataSource, rulerProperties.getRuleConfig().getTableName());
    }
}
