package info.lostred.ruler.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.engine.CompleteRulesEngine;
import info.lostred.ruler.engine.DetailRulesEngine;
import info.lostred.ruler.factory.AnnotationRuleFactory;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.factory.RulesEngineFactory;
import info.lostred.ruler.support.TypeReference;
import info.lostred.ruler.test.config.RulerConfig;
import info.lostred.ruler.test.domain.model.SubValidClass;
import info.lostred.ruler.test.domain.model.ValidClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class AnnotationInitSmokeTest {
    static String validClassName = SubValidClass.class.getName();
    static String businessType = RulerConstants.DEFAULT_BUSINESS_TYPE;
    static RuleFactory ruleFactory;
    static DetailRulesEngine<ValidClass> engine;
    static Collection<ValidInfo> validInfos;
    static ValidConfiguration validConfiguration;
    ObjectMapper objectMapper = new ObjectMapper();

    String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    static ValidConfiguration buildValidInfos() {
        validInfos = new ArrayList<>();
        ValidInfo validInfo1 = ValidInfo.ofRequired("string", validClassName);
        ValidInfo validInfo2 = ValidInfo.ofRequired("number", validClassName);
        ValidInfo validInfo3 = ValidInfo.ofRequired( "time", validClassName);
        ValidInfo validInfo4 = ValidInfo.ofDict( "string", validClassName);
        ValidInfo validInfo5 = ValidInfo.ofNumberScope("number", null, BigDecimal.TEN, validClassName);
        ValidInfo validInfo6 = ValidInfo.ofDateTimeScope("time", null, LocalDateTime.now(), validClassName);
        validInfos.add(validInfo1);
        validInfos.add(validInfo2);
        validInfos.add(validInfo3);
        validInfos.add(validInfo4);
        validInfos.add(validInfo5);
        validInfos.add(validInfo6);
        return new ValidConfiguration(validInfos);
    }

    @BeforeAll
    static void init() {
        validConfiguration = buildValidInfos();
        ruleFactory = new AnnotationRuleFactory(validConfiguration, RulerConfig.class);
        TypeReference<CompleteRulesEngine<ValidClass>> typeReference = new TypeReference<CompleteRulesEngine<ValidClass>>() {
        };
        engine = RulesEngineFactory.builder(ruleFactory, businessType, typeReference).build();
    }

    @Test
    void sample1() throws JsonProcessingException {
        ValidClass validClass = new ValidClass();
        validClass.setNumber(BigDecimal.ZERO);
        SubValidClass subValidClass = new SubValidClass();
        subValidClass.setNumber(new BigDecimal(11));
        subValidClass.setTime(LocalDateTime.now());
        validClass.setSubValidClasses(Collections.singletonList(subValidClass));

        Result result = engine.execute(validClass);
        System.out.println(this.toJson(result));
    }
}
