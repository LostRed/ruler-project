package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.GenericRule;

@Rule(ruleCode = "姓名必填",
        businessType = "person",
        description = "姓名不能为空",
        parameterExp = "name")
public class NameRule extends GenericRule<String> {
    public NameRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean doJudge(String value) {
        return value == null || value.isEmpty();
    }
}
