package com.ylzinfo.ruler.factory;

import com.ylzinfo.ruler.autoconfigure.RulerProperties;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.jdbc.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 数据库规则管理器
 *
 * @author dengluwei
 */
public class DbRuleFactory extends AbstractRuleFactory {
    private final static String RULE_INFO_TABLE_NAME = "ruler_rule_info";
    private final JdbcTemplate jdbcTemplate;
    private final RulerProperties rulerProperties;

    public DbRuleFactory(ValidConfiguration validConfiguration, JdbcTemplate jdbcTemplate, RulerProperties rulerProperties) {
        super(validConfiguration);
        this.jdbcTemplate = jdbcTemplate;
        this.rulerProperties = rulerProperties;
        this.init();
    }

    @Override
    public void init() {
        String ruleInfoTableName = rulerProperties.getRuleConfig().getTableName();
        jdbcTemplate.execute(JdbcUtils.parseSql("create-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName));
        jdbcTemplate.execute(JdbcUtils.parseSql("insert-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName));
        String defaultBusinessType = rulerProperties.getDefaultBusinessType();
        String sql = JdbcUtils.parseSql("select-rule-info", RULE_INFO_TABLE_NAME, ruleInfoTableName);
        List<RuleInfo> ruleInfos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RuleInfo.class), defaultBusinessType);
        for (RuleInfo ruleInfo : ruleInfos) {
            try {
                String validClassName = ruleInfo.getValidClassName();
                Class<?> validClass = this.getClass().getClassLoader().loadClass(validClassName);
                ruleInfo.setValidClass(validClass);
                super.ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
                AbstractRule<?> rule = builder(super.validConfiguration, ruleInfo, validClass).build();
                super.rules.put(ruleInfo.getRuleCode(), rule);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
