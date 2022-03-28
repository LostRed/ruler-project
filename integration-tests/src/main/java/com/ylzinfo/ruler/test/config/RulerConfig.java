package com.ylzinfo.ruler.test.config;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.constants.ValidType;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.ValidInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RuleScan("com.ylzinfo.ruler.test.rule")
public class RulerConfig {
    private static final String validClassName = "com.ylzinfo.ruler.test.entity.User";
    private static final String businessType = "user";

    @Bean
    public ValidConfiguration validConfiguration() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = new ValidInfo("1", businessType, ValidType.REQUIRED.name(), "username", validClassName);
        ValidInfo validInfo2 = new ValidInfo("2", businessType, ValidType.REQUIRED.name(), "number", validClassName);
        ValidInfo validInfo3 = new ValidInfo("3", businessType, ValidType.REQUIRED.name(), "time", validClassName);
        ValidInfo validInfo4 = new ValidInfo("4", businessType, ValidType.DICT.name(), "string", validClassName);
        ValidInfo validInfo5 = new ValidInfo("5", businessType, ValidType.NUMBER_SCOPE.name(), "number", validClassName);
        validInfo5.setUpperLimit(BigDecimal.TEN);
        ValidInfo validInfo6 = new ValidInfo("6", businessType, ValidType.DATETIME_SCOPE.name(), "time", validClassName);
        validInfo6.setEndTime(LocalDateTime.now());
        validInfos.add(validInfo1);
        validInfos.add(validInfo2);
        validInfos.add(validInfo3);
        validInfos.add(validInfo4);
        validInfos.add(validInfo5);
        validInfos.add(validInfo6);
        ValidConfiguration validConfiguration = new ValidConfiguration(validInfos);
        Map<String, Set<Object>> dict = new HashMap<>();
        Set<Object> set = new HashSet<>(Arrays.asList("hello", "world"));
        dict.put("string", set);
        validConfiguration.addDict(dict);
        return validConfiguration;
    }
}
