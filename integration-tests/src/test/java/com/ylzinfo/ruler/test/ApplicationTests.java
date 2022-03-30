package com.ylzinfo.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.test.entity.Area;
import com.ylzinfo.ruler.test.entity.Contact;
import com.ylzinfo.ruler.test.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class ApplicationTests {
    static String businessType = "person";
    @Autowired
    RulesEngineFactory rulesEngineFactory;
    @Autowired
    ObjectMapper objectMapper;

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @Test
    void rulesEngineFactoryTest() throws JsonProcessingException, ParseException {
        Person person = new Person();
        person.setCertNo("12312");
        person.setAge(10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("1992-01-01");
        person.setBirthday(parse);
        Area area = new Area();

        person.setArea(area);
        Contact contact = new Contact();
        contact.setPassword("1234");
        person.setContacts(Arrays.asList(contact));
        RulesEngine<Person> rulesEngine = rulesEngineFactory.dispatch(businessType, person, Person.class);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        System.out.println(toJson(result));
        long e = System.currentTimeMillis();
        System.out.println("执行时间: " + (e - s) + " ms");
    }
}
