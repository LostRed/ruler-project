package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.rule.SimpleRule;
import info.lostred.ruler.test.domain.Person;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Rule(ruleCode = "姓名必填",
        businessType = "person",
        description = "姓名不能为空")
public class NameRule extends SimpleRule<Person> {
    @Resource
    private String name;

    @Override
    public Object getValueInternal(Person person) {
        return name;
    }

    @Override
    public boolean supportsInternal(Person person) {
        return !ObjectUtils.isEmpty(person);
    }
}
