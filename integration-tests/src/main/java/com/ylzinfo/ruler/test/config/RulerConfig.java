package com.ylzinfo.ruler.test.config;

import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.constants.ValidType;
import com.ylzinfo.ruler.core.GlobalConfiguration;
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
    public GlobalConfiguration globalConfiguration() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = ValidInfo.ofRequired("name", Person.class.getName());
        ValidInfo validInfo2 = ValidInfo.ofRequired("gender", Person.class.getName());
        ValidInfo validInfo3 = ValidInfo.ofDict(ValidType.DICT.name(), "gender", Person.class.getName());
        ValidInfo validInfo4 = ValidInfo.ofNumberScope("age", new BigDecimal(18), null, Person.class.getName());
        ValidInfo validInfo5 = ValidInfo.ofDateTimeScope("birthday",
                LocalDateTime.of(1990, 1, 1, 0, 0, 0),
                LocalDateTime.of(2004, 1, 1, 0, 0, 0),
                Person.class.getName());
        ValidInfo validInfo6 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "type", Contact.class.getName());
        ValidInfo validInfo7 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "account", Contact.class.getName());
        ValidInfo validInfo8 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "password", Contact.class.getName());
        ValidInfo validInfo9 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "country", Area.class.getName());
        ValidInfo validInfo10 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "province", Area.class.getName());
        ValidInfo validInfo11 = ValidInfo.ofRequired(ValidType.REQUIRED.name(), "city", Area.class.getName());

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
        GlobalConfiguration globalConfiguration = new GlobalConfiguration(validInfos);

        Map<ValidInfo, Set<Object>> dict = new HashMap<>();
        Set<Object> set = new HashSet<>(Arrays.asList("1", "2"));
        dict.put(validInfo3, set);
        globalConfiguration.addDict(dict);
        return globalConfiguration;
    }
}
