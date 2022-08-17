package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.GenericDomainRule;
import info.lostred.ruler.test.domain.Person;

import java.util.HashMap;
import java.util.Map;

@Rule(ruleCode = "姓名必填",
        businessType = "person",
        description = "姓名不能为空")
public class NameRule extends GenericDomainRule<Person> {
    public NameRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean supports(Person object) {
        return true;
    }

    @Override
    protected boolean judge(Person object) {
        return object.getName() == null;
    }

    @Override
    protected Map<String, Object> collectMappings(Person object) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", object.getName());
        return map;
    }
}
