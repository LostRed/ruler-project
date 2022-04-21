package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.RulesEngineFactory;
import info.lostred.ruler.rule.DynamicRule;
import info.lostred.ruler.test.entity.Area;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class ApplicationTest {
    static String businessType = "person";
    static Person person;
    @Autowired
    RulesEngineFactory rulesEngineFactory;
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
        Date parse = simpleDateFormat.parse("2011-01-01");
        person.setBirthday(parse);
        Area area = new Area();
        person.setArea(area);
        Contact contact1 = new Contact();
        contact1.setArea(area);
        contact1.setPassword("1234");
        Contact contact2 = new Contact();
        contact2.setPassword("1234");
        person.setContacts(Arrays.asList(contact1, contact2));
    }

    @Test
    void rulesEngineFactoryTest() throws JsonProcessingException {
        RulesEngine<Person> rulesEngine = rulesEngineFactory.getEngine(businessType, person, Person.class);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        long e = System.currentTimeMillis();
        printResult(result, s, e);
    }

    @Test
    void dynamicAddRuleTest() throws JsonProcessingException {
        RulesEngine<Person> rulesEngine = rulesEngineFactory.getEngine(businessType, person, Person.class);
        RuleInfo ruleInfo = RuleInfo.of(UUID.randomUUID().toString(), "person", ValidGrade.ILLEGAL.name(), "dynamic",
                0, true, true, DynamicRule.class.getName(), Person.class);
        rulesEngine.registerRule(ruleInfo, Person.class, String.class,
                Person::getCertNo,
                null,
                e -> true,
                e -> e.length() != 18,
                e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("certNo", e);
                    return map.entrySet();
                });
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        long e = System.currentTimeMillis();
        printResult(result, s, e);
    }
}
