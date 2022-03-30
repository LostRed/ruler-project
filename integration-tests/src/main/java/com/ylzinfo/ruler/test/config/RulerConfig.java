package com.ylzinfo.ruler.test.config;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.constants.RulerConstants;
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
    private static final String validClassName1 = "com.ylzinfo.ruler.test.entity.Country";
    private static final String validClassName2 = "com.ylzinfo.ruler.test.entity.Area";
    private static final String validClassName3 = "com.ylzinfo.ruler.test.entity.User";
    private static final String businessType = RulerConstants.DEFAULT_BUSINESS_TYPE;

    @Bean
    public ValidConfiguration validConfiguration() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = new ValidInfo("1", businessType, ValidType.REQUIRED.name(), "name", validClassName1);
        ValidInfo validInfo2 = new ValidInfo("2", businessType, ValidType.REQUIRED.name(), "areaCode", validClassName2);
        ValidInfo validInfo3 = new ValidInfo("3", businessType, ValidType.DICT.name(), "name", validClassName1);
        ValidInfo validInfo4 = new ValidInfo("4", businessType, ValidType.NUMBER_SCOPE.name(), "age", validClassName3);
        ValidInfo validInfo5 = new ValidInfo("4", businessType, ValidType.DATETIME_SCOPE.name(), "birthday", validClassName3);
        validInfo4.setLowerLimit(new BigDecimal(18));
        validInfo5.setBeginTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        validInfos.add(validInfo1);
        validInfos.add(validInfo2);
        validInfos.add(validInfo3);
        validInfos.add(validInfo4);
        validInfos.add(validInfo5);
        ValidConfiguration validConfiguration = new ValidConfiguration(validInfos);
        Map<ValidInfo, Set<Object>> dict = new HashMap<>();
        Set<Object> set = new HashSet<>(Arrays.asList("hello", "world"));
        dict.put(validInfo3, set);
        validConfiguration.addDict(dict);
        return validConfiguration;
    }
}
