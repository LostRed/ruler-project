package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.utils.SqlUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Ruler配置表初始化
 *
 * @author dengluwei
 */
public class RulerConfigDatabaseInitializer implements RuleConfigInitializer {
    private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";
    private final static String RULE_INFO_TABLE_NAME = "ruler_rule_info";
    private final JdbcTemplate jdbcTemplate;
    private final RulerProperties<?> rulerProperties;

    public RulerConfigDatabaseInitializer(JdbcTemplate jdbcTemplate, RulerProperties<?> rulerProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.rulerProperties = rulerProperties;
    }

    @Override
    public void afterPropertiesSet() {
        String validInfoTableName = rulerProperties.getValidInfoTableName();
        String ruleInfoTableName = rulerProperties.getRuleInfoTableName();
        jdbcTemplate.execute(SqlUtils.parseSql("create-valid-info", VALID_INFO_TABLE_NAME, validInfoTableName));
        jdbcTemplate.execute(SqlUtils.parseSql("create-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName));
    }

    /**
     * 抓取校验信息
     *
     * @return 校验信息集合
     */
    @Override
    public List<ValidInfo> fetchValidInfo() {
        String validInfoTableName = rulerProperties.getValidInfoTableName();
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        String sql = SqlUtils.parseSql("select-valid-info", VALID_INFO_TABLE_NAME, validInfoTableName);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ValidInfo.class), defaultBusinessType);
    }

    /**
     * 抓取规则信息
     *
     * @return 校验规则集合
     */
    @Override
    public List<RuleInfo> fetchRuleInfo() {
        String ruleInfoTableName = rulerProperties.getRuleInfoTableName();
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        String sql = SqlUtils.parseSql("select-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RuleInfo.class), defaultBusinessType);
    }
}
