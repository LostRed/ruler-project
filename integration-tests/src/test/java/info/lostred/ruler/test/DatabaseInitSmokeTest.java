package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.DetailRulesEngine;
import info.lostred.ruler.factory.DatabaseRuleFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.factory.RulesEngineFactory;
import info.lostred.ruler.jdbc.RulerDateSource;
import info.lostred.ruler.support.TypeReference;
import info.lostred.ruler.test.domain.model.SubValidClass;
import info.lostred.ruler.test.domain.model.ValidClass;
import info.lostred.ruler.util.JdbcUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

class DatabaseInitSmokeTest {
    static String driverClassName = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/rules_engine";
    static String user = "rules_engine";
    static String password = "123456";
    static String businessType = RulerConstants.DEFAULT_BUSINESS_TYPE;
    static DataSource dataSource;

    static RuleFactory ruleFactory;
    static DetailRulesEngine<ValidClass> engine;
    static Collection<ValidInfo> validInfos;
    static ValidConfiguration validConfiguration;
    ObjectMapper objectMapper = new ObjectMapper();

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    static ValidConfiguration buildValidInfos() {
        String createTableSql = JdbcUtils.parseSql(RulerConstants.CREATE_VALID_INFO_SQL);
        String insertDataSql1 = JdbcUtils.parseSql("insert-valid-info");
        String insertDataSql2 = JdbcUtils.parseSql("insert-test-rule-info");
        JdbcUtils.execute(dataSource, createTableSql);
        JdbcUtils.execute(dataSource, insertDataSql1);
        JdbcUtils.execute(dataSource, insertDataSql2);
        String selectSql = JdbcUtils.parseSql(RulerConstants.SELECT_VALID_INFO_SQL);
        validInfos = JdbcUtils.query(dataSource, selectSql, ValidInfo.class, businessType);
        return new ValidConfiguration(validInfos, true);
    }

    @BeforeAll
    static void init() {
        dataSource = new RulerDateSource(driverClassName, url, user, password);
        validConfiguration = buildValidInfos();
        ruleFactory = new DatabaseRuleFactory(validConfiguration, dataSource, RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME);
        TypeReference<CompleteRulesEngine<ValidClass>> typeReference = new TypeReference<CompleteRulesEngine<ValidClass>>() {
        };
        engine = RulesEngineFactory.builder(ruleFactory, businessType, typeReference).build();
    }

    @Test
    void sample1() throws JsonProcessingException {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        subValidClass.setTime(LocalDateTime.now());
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));

        Result result = engine.execute(validClass);
        System.out.println(this.toJson(result));
    }
}
