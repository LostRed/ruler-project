package com.ylzinfo.ruler.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.core.RulesEngineManager;
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
import java.util.Collections;

@SpringBootTest
class ApplicationTests {
    @Autowired
    ObjectProvider<RulesEngine<ValidClass>> objectProvider;
    @Autowired
    RulesEngineManager rulesEngineManager;

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
            Result result = ((DetailRulesEngine<ValidClass>) rulesEngine).execute(validClass);
            System.out.println(toJson(result));
        } else if (rulesEngine instanceof SimpleRulesEngine) {
            boolean result = ((SimpleRulesEngine<ValidClass>) rulesEngine).execute(validClass);
            System.out.println(result);
        }
    }

    @Test
    void test() {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));
        RulesEngine<ValidClass> common = rulesEngineManager.dispatch("common", validClass, ValidClass.class);
        if (common instanceof DetailRulesEngine) {
            Result result = ((DetailRulesEngine<ValidClass>) common).execute(validClass);
            System.out.println(toJson(result));
        }
    }
}
