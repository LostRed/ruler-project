package com.ylzinfo.ruler.test.config;

import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.model.ValidClass;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
import com.ylzinfo.ruler.support.RuleFactory;
import com.ylzinfo.ruler.support.RulesEngineFactory;
import com.ylzinfo.ruler.support.TypeReference;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class Config {
    @Bean
    public RulesEngine<ValidClass> defaultRulesEngine() {
        List<RuleInfo> ruleInfos = new ArrayList<>();
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setRuleCode("test_1");
        ruleInfo.setBusinessType("common");
        ruleInfo.setGrade("可疑");
        ruleInfo.setDesc("测试规则");
        ruleInfo.setSeq(0);
        ruleInfo.setEnable(true);
        ruleInfo.setRuleClassName("com.ylzinfo.ruler.rule.TestRule");
        ruleInfos.add(ruleInfo);
        ValidConfiguration validConfiguration = new ValidConfiguration();
        //构建规则信息列表...
        List<Rule<ValidClass>> rules = RuleFactory.rulesBuilder(validConfiguration, ruleInfos, ValidClass.class).build();
        //定义规则引擎类型与校验对象类型
        TypeReference<SimpleRulesEngine<ValidClass>> typeReference = new TypeReference<SimpleRulesEngine<ValidClass>>() {
        };
        return RulesEngineFactory.builder(typeReference, rules).build();
    }
}
