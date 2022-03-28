package com.ylzinfo.ruler.factory;

import com.ylzinfo.ruler.constants.RulerConstants;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.utils.JdbcUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库规则管理器
 *
 * @author dengluwei
 */
public class DatabaseRuleFactory extends AbstractRuleFactory {
    private final DataSource dataSource;
    private final String ruleInfoTableName;

    public DatabaseRuleFactory(ValidConfiguration validConfiguration, DataSource dataSource, String ruleInfoTableName) {
        super(validConfiguration);
        this.dataSource = dataSource;
        this.ruleInfoTableName = ruleInfoTableName;
        this.init();
    }

    @Override
    public void init() {
        String createTableSql = JdbcUtils.parseSql(RulerConstants.CREATE_RULE_INFO_SQL,
                RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME, ruleInfoTableName);
        String insertDataSql = JdbcUtils.parseSql(RulerConstants.INSERT_RULE_INFO_SQL,
                RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME, ruleInfoTableName);
        JdbcUtils.execute(dataSource, createTableSql);
        JdbcUtils.execute(dataSource, insertDataSql);
        String selectSql = JdbcUtils.parseSql(RulerConstants.SELECT_RULE_INFO_SQL,
                RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME, ruleInfoTableName);
        List<RuleInfo> ruleInfos = JdbcUtils.query(dataSource, selectSql, RuleInfo.class);
        for (RuleInfo ruleInfo : ruleInfos) {
            try {
                String validClassName = ruleInfo.getValidClassName();
                Class<?> validClass = this.getClass().getClassLoader().loadClass(validClassName);
                ruleInfo.setValidClass(validClass);
                this.ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
                AbstractRule<?> rule = builder(super.validConfiguration, ruleInfo, validClass).build();
                this.rules.put(ruleInfo.getRuleCode(), rule);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
