package com.ylzinfo.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.test.entity.Area;
import com.ylzinfo.ruler.test.entity.Country;
import com.ylzinfo.ruler.test.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class ApplicationTests {
    static String businessType = "user";
    @Autowired
    RulesEngineFactory rulesEngineFactory;
    @Autowired
    ObjectMapper objectMapper;

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @Test
    void rulesEngineFactoryTest() throws JsonProcessingException, ParseException {
        User user = new User();
        user.setPassword("12312");
        Area area = new Area();
        Country country = new Country();
        Country country2 = new Country();
        country.setName("china");
        country2.setName("china2");
        user.setAge(10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse("1992-01-01");
        user.setBirthday(parse);
        area.setCountries(Arrays.asList(country, country2));
        user.setArea(area);
        RulesEngine<User> rulesEngine = rulesEngineFactory.dispatch(businessType, user, User.class);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(user);
        System.out.println(toJson(result));
        long e = System.currentTimeMillis();
        System.out.println("执行时间: " + (e - s) + " ms");
    }
}
