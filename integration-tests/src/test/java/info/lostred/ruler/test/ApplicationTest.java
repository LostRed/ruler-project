package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.core.RulesEngine;
import info.lostred.ruler.factory.RulesEngineFactory;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.test.entity.Area;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class ApplicationTest {
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
        RulesEngine<Person> rulesEngine = rulesEngineFactory.getEngine(businessType, person, Person.class);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(person);
        System.out.println(toJson(result));
        long e = System.currentTimeMillis();
        System.out.println("执行时间: " + (e - s) + " ms");
    }
}
