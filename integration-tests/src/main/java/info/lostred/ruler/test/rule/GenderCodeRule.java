package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;

@Rule(ruleCode = "性别码值",
        businessType = "person",
        description = "性别必须是字典值",
        parameterExp = "gender",
        conditionExp = "gender!=null",
        predicateExp = "!@dict.get('gender').contains(gender)")
public class GenderCodeRule extends AbstractRule {
    public GenderCodeRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
