package com.ylzinfo.ruler.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.test.entity.Area;
import com.ylzinfo.ruler.test.entity.Country;
import com.ylzinfo.ruler.test.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    static String businessType = "user";
    @Autowired
    RulesEngineFactory rulesEngineFactory;

    String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);
    }

    @Test
    void rulesEngineFactoryTest() {
        User user = new User();
        user.setPassword("12312");
        Area area = new Area();
        Country country = new Country();
        country.setName("china");
        area.setCountry(country);
        user.setArea(area);
        RulesEngine<User> rulesEngine = rulesEngineFactory.dispatch(businessType, user, User.class);
        long s = System.currentTimeMillis();
        Result result = rulesEngine.execute(user);
        long e = System.currentTimeMillis();
        System.out.println("执行时间: " + (e - s) + " ms");
        System.out.println(toJson(result));
    }
}
