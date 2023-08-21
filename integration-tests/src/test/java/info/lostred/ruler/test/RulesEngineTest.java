package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.factory.RulesEngineFactory;
import info.lostred.ruler.rule.DeclarativeRule;
import info.lostred.ruler.test.domain.Area;
import info.lostred.ruler.test.domain.Contact;
import info.lostred.ruler.test.domain.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class RulesEngineTest {
    final static String businessType = "person";
    static Person person;
    @Autowired
    RulesEngineFactory rulesEngineFactory;
    @Autowired
    RuleFactory ruleFactory;
    @Autowired
    ObjectMapper objectMapper;

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    void printResult(Object result, long startTime, long endTime) throws JsonProcessingException {
        System.out.println(toJson(result));
        System.out.println("执行时间: " + (endTime - startTime) + " ms");
    }

    @BeforeAll
    static void init() throws ParseException {
        person = new Person();
        person.setCertNo("12312");
        person.setGender("男");
        person.setAge(10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("2019-01-01");
        person.setBirthday(parse);
        Area area = new Area();
        area.setCity("广州");
        person.setArea(area);
        Contact contact1 = new Contact();
        contact1.setArea(area);
        contact1.setType("qq");
        contact1.setPassword("1234");
        Contact contact2 = new Contact();
        contact2.setPassword("12345");
        person.setContacts(Arrays.asList(contact1, contact2));
    }

    @Test
    void executeTest() throws JsonProcessingException {
        RulesEngine rulesEngine = rulesEngineFactory.getEngine(businessType);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        long e = System.currentTimeMillis();
        printResult(result, s, e);
    }

    @Test
    void registerRuleDefinitionAndExecuteTest() throws JsonProcessingException {
        RuleDefinition ruleDefinition = RuleDefinition.of("test", businessType, "", Grade.ILLEGAL, "this is a test rule",
                0, false, true, DeclarativeRule.class, "#root", "true", "true");
        ruleFactory.registerRuleDefinition(ruleDefinition);
        RulesEngine rulesEngine = rulesEngineFactory.getEngine(businessType);
        rulesEngine.addRule(ruleDefinition.getRuleCode());
        this.executeTest();
    }
}
