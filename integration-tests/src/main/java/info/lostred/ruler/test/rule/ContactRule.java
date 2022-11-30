package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;

import java.util.List;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "测试",
        parameterExp = "contacts?.![password]")
public class ContactRule extends ProgrammaticRule<List<String>> {
    public ContactRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean doSupports(List<String> value) {
        return value != null && !value.isEmpty();
    }

    @Override
    protected boolean doJudge(List<String> value) {
        return value.stream().anyMatch(e -> e.equals("1234"));
    }
}
