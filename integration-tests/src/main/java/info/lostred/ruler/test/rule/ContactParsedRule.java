package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;
import info.lostred.ruler.test.domain.Contact;

import java.util.List;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "测试",
        parameterExp = "contacts")
public class ContactParsedRule extends ProgrammaticRule<List<Contact>> {
    public ContactParsedRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean doSupports(List<Contact> value) {
        return value != null && !value.isEmpty();
    }

    @Override
    protected boolean doJudge(List<Contact> value) {
        return value.stream().anyMatch(e -> e.getPassword().equals("1234"));
    }
}
