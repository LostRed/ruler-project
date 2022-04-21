package info.lostred.ruler.test.config;

import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.test.entity.Area;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RuleScan("info.lostred.ruler.test.rule")
public class RulerConfig {
    private static final String businessType = "person";
    @Autowired
    private ValidConfiguration validConfiguration;

    @PostConstruct
    public void init() {
        Collection<ValidInfo> validInfos = new ArrayList<>();
        ValidInfo validInfo1 = ValidInfo.ofRequired(businessType, "name", Person.class.getName());
        ValidInfo validInfo2 = ValidInfo.ofRequired(businessType, "gender", Person.class.getName());
        ValidInfo validInfo3 = ValidInfo.ofDict(businessType, "gender", Person.class.getName());
        Set<Object> set = new HashSet<>(Arrays.asList("1", "2"));
        validInfo3.setDict(set);
        ValidInfo validInfo4 = ValidInfo.ofNumberScope(businessType, "age", new BigDecimal(18), null, Person.class.getName());
        ValidInfo validInfo5 = ValidInfo.ofDateTimeScope(businessType, "birthday",
                LocalDateTime.of(1990, 1, 1, 0, 0, 0),
                LocalDateTime.of(2004, 1, 1, 0, 0, 0),
                Person.class.getName());
        ValidInfo validInfo6 = ValidInfo.ofRequired(businessType, "type", Contact.class.getName());
        ValidInfo validInfo7 = ValidInfo.ofRequired(businessType, "account", Contact.class.getName());
        ValidInfo validInfo8 = ValidInfo.ofRequired(businessType, "password", Contact.class.getName());
        ValidInfo validInfo9 = ValidInfo.ofRequired(businessType, "country", Area.class.getName());
        ValidInfo validInfo10 = ValidInfo.ofRequired(businessType, "province", Area.class.getName());
        ValidInfo validInfo11 = ValidInfo.ofRequired(businessType, "city", Area.class.getName());
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
        validConfiguration.addValidInfo(validInfos);
    }
}
