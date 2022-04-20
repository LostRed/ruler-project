package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.factory.AnnotationRuleFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.proxy.DynamicRuleProxy;
import info.lostred.ruler.rule.DynamicRule;
import info.lostred.ruler.test.entity.Person;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DynamicRuleTest {
    ObjectMapper objectMapper = new ObjectMapper();

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @Test
    void test() throws JsonProcessingException {
        RuleInfo ruleInfo = RuleInfo.of(UUID.randomUUID().toString(), "common", ValidGrade.ILLEGAL.name(), "dynamic",
                0, true, true, DynamicRule.class.getName(), Person.class);
        DynamicRule<Person, String> rule = DynamicRuleProxy.builder(ruleInfo, Person.class, String.class, false)
                .setGetNode(Person::getCertNo)
                .setIsSupported(e -> true)
                .setJudge(e -> e.length() != 18)
                .setCollectEntries(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("certNo", e);
                    return map.entrySet();
                })
                .build();
        RuleFactory ruleFactory = new AnnotationRuleFactory(null, null);
        CompleteRulesEngine<Person> engine = new CompleteRulesEngine<>(ruleFactory, "common");
        engine.registerRule(rule);
        Person person = new Person();
        person.setCertNo("2123");
        Result result = engine.execute(person);
        System.out.println(toJson(result));
    }
}
