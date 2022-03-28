package com.ylzinfo.ruler.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineFactory;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.domain.model.SubValidClass;
import com.ylzinfo.ruler.domain.model.ValidClass;
import com.ylzinfo.ruler.engine.DetailRulesEngine;
import com.ylzinfo.ruler.engine.SimpleRulesEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest
class ApplicationTests {
    static String businessType = "common";
    @Autowired
    ObjectProvider<RulesEngine<ValidClass>> objectProvider;
    @Autowired
    RulesEngineFactory rulesEngineFactory;

    String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty);
    }

    @Test
    void contextLoads() {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        RulesEngine<ValidClass> rulesEngine = objectProvider.getObject();
        if (rulesEngine instanceof DetailRulesEngine) {
            Result result = rulesEngine.execute(validClass);
            System.out.println(toJson(result));
        } else if (rulesEngine instanceof SimpleRulesEngine) {
            boolean result = rulesEngine.check(validClass);
            System.out.println(result);
        }
    }

    @Test
    void rulesEngineFactoryTest() {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setString("test");
        subValidClass.setNumber(new BigDecimal(11));
        subValidClass.setTime(LocalDateTime.now());
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        RulesEngine<ValidClass> rulesEngine = rulesEngineFactory.dispatch(businessType, validClass, ValidClass.class);
        if (rulesEngine instanceof DetailRulesEngine) {
            long s = System.currentTimeMillis();
            Result result = rulesEngine.execute(validClass);
            long e = System.currentTimeMillis();
            System.out.println("执行时间: " + (e - s) + " ms");
            System.out.println(toJson(result));
        }
    }
}
