package com.ylzinfo.ruler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ylzinfo.ruler.constants.ValidGrade;
import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.*;
import com.ylzinfo.ruler.domain.model.SubValidClass;
import com.ylzinfo.ruler.domain.model.ValidClass;
import com.ylzinfo.ruler.engine.DetailRulesEngine;
import com.ylzinfo.ruler.engine.IncompleteRulesEngine;
import com.ylzinfo.ruler.support.RuleFactory;
import com.ylzinfo.ruler.support.RulesEngineFactory;
import com.ylzinfo.ruler.support.TypeReference;
import com.ylzinfo.ruler.utils.SqlUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IncompleteRulesEngineSmokeTest {
    private final static String BUSINESS_TYPE = "common";
    private final static String TEST_BUSINESS_TYPE = "test";
    private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";
    private final static String RULE_INFO_TABLE_NAME = "ruler_rule_info";

    @Autowired
    JdbcTemplate jdbcTemplate;
    DetailRulesEngine<ValidClass> rulesEngine;
    ValidConfiguration config = new ValidConfiguration();
    Result result;

    @BeforeEach
    void databaseInit() {
        jdbcTemplate.execute(SqlUtils.parseSql("create-valid-info", VALID_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("create-rule-info", RULE_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-valid-info", VALID_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-rule-info", RULE_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-test-rule-info", RULE_INFO_TABLE_NAME));
    }

    @BeforeEach
    void configurationInit() {
        List<ValidInfo> validInfos = jdbcTemplate.query(SqlUtils.parseSql("select-valid-info", VALID_INFO_TABLE_NAME),
                new BeanPropertyRowMapper<>(ValidInfo.class), BUSINESS_TYPE);
        config.addValidInfo(validInfos);
        Map<String, Set<Object>> map = new HashMap<>();
        map.put("string", new HashSet<>(Arrays.asList("hello", "world")));
        config.setDictType(map);
    }

    @BeforeEach
    void ruleEngineInit() {
        if (rulesEngine == null) {
            //构建规则集合
            List<RuleInfo> ruleInfos = jdbcTemplate.query(SqlUtils.parseSql("select-rule-info", RULE_INFO_TABLE_NAME),
                    new BeanPropertyRowMapper<>(RuleInfo.class), BUSINESS_TYPE);
            List<RuleInfo> test = jdbcTemplate.query(SqlUtils.parseSql("select-rule-info", RULE_INFO_TABLE_NAME),
                    new BeanPropertyRowMapper<>(RuleInfo.class), TEST_BUSINESS_TYPE);
            ruleInfos.addAll(test);
            List<Rule<ValidClass>> rules = RuleFactory.rulesBuilder(config, ruleInfos, ValidClass.class).build();
            //构建规则引擎
            TypeReference<IncompleteRulesEngine<ValidClass>> typeReference = new TypeReference<IncompleteRulesEngine<ValidClass>>() {
            };
            rulesEngine = RulesEngineFactory.builder(typeReference, rules).build();
        }
    }

    @AfterEach
    void printResultAsJsonString() {
        String json = JSON.toJSONString(result, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);
        System.out.println(json);
    }

    @Test
    void sample1() {
        ValidClass validClass = new ValidClass();
        SubValidClass subValidClass = new SubValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        result = rulesEngine.execute(validClass);
        List<Report> reports = result.getReports();
        Assertions.assertEquals(ValidGrade.ILLEGAL.getText(), result.getGrade());
        Assertions.assertEquals(1, reports.size());
        long suspected = reports.stream()
                .map(e -> e.getRuleInfo().getGrade())
                .filter(ValidGrade.SUSPECTED.getText()::equals)
                .count();
        long illegal = reports.stream()
                .map(e -> e.getRuleInfo().getGrade())
                .filter(ValidGrade.ILLEGAL.getText()::equals)
                .count();
        Assertions.assertEquals(0, suspected);
        Assertions.assertEquals(1, illegal);
    }
}
