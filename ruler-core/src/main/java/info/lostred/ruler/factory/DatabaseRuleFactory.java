package info.lostred.ruler.factory;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.exception.RuleInitException;
import info.lostred.ruler.util.JdbcUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库规则管理器
 *
 * @author lostred
 */
public class DatabaseRuleFactory extends AbstractRuleFactory {
    private final DataSource dataSource;
    private final String ruleInfoTableName;

    public DatabaseRuleFactory(GlobalConfiguration globalConfiguration, DataSource dataSource, String ruleInfoTableName) {
        super(globalConfiguration);
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
                this.registerRuleInfo(ruleInfo);
                this.ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
            } catch (ClassNotFoundException e) {
                throw new RuleInitException(e, ruleInfo);
            }
        }
        for (String ruleCode : this.ruleInfoMap.keySet()) {
            RuleInfo ruleInfo = this.ruleInfoMap.get(ruleCode);
            this.createRule(ruleInfo);
        }
    }
}
