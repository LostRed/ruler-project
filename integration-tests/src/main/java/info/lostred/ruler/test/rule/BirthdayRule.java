package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;

@Rule(ruleCode = "生日范围",
        businessType = "person",
        description = "生日必须在范围[2020-01-01,2020-12-31]之间",
        parameterExp = "birthday",
        conditionExp = "birthday!=null",
        predicateExp = "!T(info.lostred.ruler.util.DateTimeUtils).between(birthday,'2020-01-01','2020-12-31')")
public class BirthdayRule extends AbstractRule {
    public BirthdayRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
