package info.lostred.ruler.test.elem;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.rule.SimpleRule;
import info.lostred.ruler.test.domain.Area;
import info.lostred.ruler.test.domain.Person;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Set;

@Rule(ruleCode = "地区城市枚举规则",
        businessType = "person",
        description = "地区中的城市必须是枚举值")
public class AreaCityEnumRule extends SimpleRule<Person> {
    @Resource
    private Set<String> areaCities;

    @Override
    public Object getValueInternal(Person person) {
        Area area = person.getArea();
        String city = area.getCity();
        if (!areaCities.contains(city)) {
            return city;
        }
        return null;
    }

    @Override
    public boolean supportsInternal(Person person) {
        return !ObjectUtils.isEmpty(person)
                && !ObjectUtils.isEmpty(person.getArea())
                && !ObjectUtils.isEmpty(person.getArea().getCity());
    }
}
