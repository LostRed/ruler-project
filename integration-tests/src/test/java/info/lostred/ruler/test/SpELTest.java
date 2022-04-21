package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.Collections;

public class SpELTest {
    ObjectMapper objectMapper = new ObjectMapper();

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @Test
    void test() throws JsonProcessingException {
        RuleDefinition ruleDefinition = RuleDefinition.of("test", "test", Grade.ILLEGAL, "密码长度小于6", AbstractRule.class,
                "contacts[#i].password", "!=null", ".length<6", 0);
        ExpressionParser parser = new SpelExpressionParser();
        AbstractRule rule = new AbstractRule(ruleDefinition, new SpelExpressionParser());
        RulesEngine rulesEngine = new RulesEngine("common", parser, Collections.singletonList(rule));
        rulesEngine.setVariable("set", Arrays.asList("1", "2", "3"));
        Person person = new Person();
        person.setCertNo("12314");
        Contact contact1 = new Contact();
        contact1.setPassword("1234");
        Contact contact2 = new Contact();
        contact2.setPassword("1234");
        person.setContacts(Arrays.asList(contact1, contact2));
        Result result = rulesEngine.execute(person);
        System.out.println(toJson(result));
    }
}
