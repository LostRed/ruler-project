package com.ylzinfo.ruler.test.config;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.constants.ValidType;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.test.entity.Area;
import com.ylzinfo.ruler.test.entity.Contact;
import com.ylzinfo.ruler.test.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RuleScan("com.ylzinfo.ruler.test.rule")
public class RulerConfig {

    @Bean
    public ValidConfiguration validConfiguration() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = new ValidInfo("1", ValidType.REQUIRED.name(), "name", Person.class.getName());
        ValidInfo validInfo2 = new ValidInfo("2", ValidType.REQUIRED.name(), "gender", Person.class.getName());
        ValidInfo validInfo3 = new ValidInfo("3", ValidType.DICT.name(), "gender", Person.class.getName());
        ValidInfo validInfo4 = new ValidInfo("4", ValidType.NUMBER_SCOPE.name(), "age", Person.class.getName());
        ValidInfo validInfo5 = new ValidInfo("5", ValidType.DATETIME_SCOPE.name(), "birthday", Person.class.getName());
        ValidInfo validInfo6 = new ValidInfo("6", ValidType.REQUIRED.name(), "type", Contact.class.getName());
        ValidInfo validInfo7 = new ValidInfo("7", ValidType.REQUIRED.name(), "account", Contact.class.getName());
        ValidInfo validInfo8 = new ValidInfo("8", ValidType.REQUIRED.name(), "password", Contact.class.getName());
        ValidInfo validInfo9 = new ValidInfo("9", ValidType.REQUIRED.name(), "country", Area.class.getName());
        ValidInfo validInfo10 = new ValidInfo("10", ValidType.REQUIRED.name(), "province", Area.class.getName());
        ValidInfo validInfo11 = new ValidInfo("11", ValidType.REQUIRED.name(), "city", Area.class.getName());
        validInfo4.setLowerLimit(new BigDecimal(18));
        validInfo5.setBeginTime(LocalDateTime.of(1990, 1, 1, 0, 0, 0));
        validInfo5.setEndTime(LocalDateTime.of(2004, 1, 1, 0, 0, 0));

        validInfos.add(validInfo1);
        validInfos.add(validInfo2);
        validInfos.add(validInfo3);
        validInfos.add(validInfo4);
        validInfos.add(validInfo5);
        validInfos.add(validInfo6);
        validInfos.add(validInfo7);
        validInfos.add(validInfo8);
        validInfos.add(validInfo9);
        validInfos.add(validInfo10);
        validInfos.add(validInfo11);
        ValidConfiguration validConfiguration = new ValidConfiguration(validInfos);

        Map<ValidInfo, Set<Object>> dict = new HashMap<>();
        Set<Object> set = new HashSet<>(Arrays.asList("1", "2"));
        dict.put(validInfo3, set);
        validConfiguration.addDict(dict);
        return validConfiguration;
    }
}
