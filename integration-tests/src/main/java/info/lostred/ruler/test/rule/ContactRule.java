package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;

import java.util.List;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "测试",
        parameterExp = "contacts?.![password].?[#this.equals('1234')]")
public class ContactRule extends ProgrammaticRule<List<String>> {
    public ContactRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean supportsInternal(List<String> value) {
        return value != null && !value.isEmpty();
    }

    @Override
    protected boolean evaluateInternal(List<String> value) {
        return value.stream().anyMatch(e -> e.equals("1234"));
    }
}
