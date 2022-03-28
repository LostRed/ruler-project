package com.ylzinfo.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylzinfo.ruler.constants.RulerConstants;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.engine.CompleteRulesEngine;
import com.ylzinfo.ruler.engine.DetailRulesEngine;
import com.ylzinfo.ruler.factory.DatabaseRuleFactory;
import com.ylzinfo.ruler.factory.DefaultRulesEngineFactory;
import com.ylzinfo.ruler.factory.RuleFactory;
import com.ylzinfo.ruler.jdbc.RulerDateSource;
import com.ylzinfo.ruler.support.TypeReference;
import com.ylzinfo.ruler.test.domain.model.SubValidClass;
import com.ylzinfo.ruler.test.domain.model.ValidClass;
import com.ylzinfo.ruler.utils.JdbcUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class DatabaseInitSmokeTest {
    static String driverClassName = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/rules_engine";
    static String user = "rules_engine";
    static String password = "123456";
    static String businessType = "common";
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
        validInfos = JdbcUtils.query(dataSource, selectSql, ValidInfo.class);
        return new ValidConfiguration(validInfos);
    }

    @BeforeAll
    static void init() {
        dataSource = new RulerDateSource(driverClassName, url, user, password);
        validConfiguration = buildValidInfos();
        ruleFactory = new DatabaseRuleFactory(validConfiguration, dataSource, RulerConstants.ORIGIN_RULE_INFO_TABLE_NAME);
        TypeReference<CompleteRulesEngine<ValidClass>> typeReference = new TypeReference<CompleteRulesEngine<ValidClass>>() {
        };
        engine = DefaultRulesEngineFactory.builder(ruleFactory, businessType, typeReference).build();
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
