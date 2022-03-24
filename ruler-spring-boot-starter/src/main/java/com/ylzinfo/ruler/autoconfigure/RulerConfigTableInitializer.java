package com.ylzinfo.ruler.autoconfigure;

import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.utils.SqlUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Ruler配置表初始化
 *
 * @author dengluwei
 */
public class RulerConfigTableInitializer implements InitializingBean {
    private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";
    private final static String RULE_INFO_TABLE_NAME = "ruler_rule_info";
    private final JdbcTemplate jdbcTemplate;

    public RulerConfigTableInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        jdbcTemplate.execute(SqlUtils.parseSql("create-valid-info", VALID_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("create-rule-info", RULE_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-rule-info", RULE_INFO_TABLE_NAME));
    }

    public <E> List<ValidInfo> fetchValidInfo(RulerProperties<E> rulerProperties) {
        String validInfoTableName = rulerProperties.getValidInfoTableName();
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        String sql = SqlUtils.parseSql("select-valid-info", validInfoTableName);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ValidInfo.class), defaultBusinessType);
    }

    public <E> List<RuleInfo> fetchRuleInfo(RulerProperties<E> rulerProperties) {
        String ruleInfoTableName = rulerProperties.getRuleInfoTableName();
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        String sql = SqlUtils.parseSql("select-rule-info", ruleInfoTableName);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RuleInfo.class), defaultBusinessType);
    }
}
