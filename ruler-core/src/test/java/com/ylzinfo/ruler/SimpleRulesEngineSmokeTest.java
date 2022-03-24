package com.ylzinfo.ruler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.domain.model.SubValidClass;
import com.ylzinfo.ruler.domain.model.ValidClass;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
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
class SimpleRulesEngineSmokeTest {
    private final static String BUSINESS_TYPE = "common";
    private final static String TEST_BUSINESS_TYPE = "test";
    private final static String VALID_INFO_TABLE_NAME = "ruler_valid_info";
    private final static String RULE_INFO_TABLE_NAME = "ruler_rule_info";

    @Autowired
    JdbcTemplate jdbcTemplate;
    SimpleRulesEngine<ValidClass> rulesEngine;
    ValidConfiguration config = new ValidConfiguration();
    boolean illegal;
    boolean suspected;

    @BeforeEach
    void databaseInit() {
        jdbcTemplate.execute(SqlUtils.parseSql("create-valid-info", "ruler_valid_info", VALID_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("create-rule-info", "ruler_rule_info", RULE_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-valid-info", "ruler_valid_info", VALID_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-rule-info", "ruler_rule_info", RULE_INFO_TABLE_NAME));
        jdbcTemplate.execute(SqlUtils.parseSql("insert-test-rule-info", "ruler_rule_info", RULE_INFO_TABLE_NAME));
    }

    @BeforeEach
    void configurationInit() {
        List<ValidInfo> validInfos = jdbcTemplate.query(SqlUtils.parseSql("select-valid-info", "ruler_valid_info", VALID_INFO_TABLE_NAME),
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
            List<RuleInfo> ruleInfos = jdbcTemplate.query(SqlUtils.parseSql("select-rule-info", "ruler_rule_info", RULE_INFO_TABLE_NAME),
                    new BeanPropertyRowMapper<>(RuleInfo.class), BUSINESS_TYPE);
            List<RuleInfo> test = jdbcTemplate.query(SqlUtils.parseSql("select-rule-info", "ruler_rule_info", RULE_INFO_TABLE_NAME),
                    new BeanPropertyRowMapper<>(RuleInfo.class), TEST_BUSINESS_TYPE);
            ruleInfos.addAll(test);
            List<Rule<ValidClass>> rules = RuleFactory.rulesBuilder(config, ruleInfos, ValidClass.class).build();
            //构建规则引擎
            TypeReference<SimpleRulesEngine<ValidClass>> typeReference = new TypeReference<SimpleRulesEngine<ValidClass>>() {
            };
            rulesEngine = RulesEngineFactory.builder(typeReference, rules).build();
        }
    }

    @AfterEach
    void printResultAsJsonString() {
        String json = JSON.toJSONString(illegal, SerializerFeature.PrettyFormat,
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
        illegal = rulesEngine.execute(validClass);
        Assertions.assertTrue(illegal);
        suspected = rulesEngine.checkSuspicious(validClass);
        Assertions.assertTrue(suspected);
    }
}
