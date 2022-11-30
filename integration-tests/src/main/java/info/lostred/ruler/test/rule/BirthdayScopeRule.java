package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.DeclarativeRule;

@Rule(ruleCode = "生日范围",
        businessType = "person",
        description = "生日必须在范围[2020-01-01,2020-12-31]之间",
        parameterExp = "birthday",
        conditionExp = "birthday!=null",
        predicateExp = "!#dateBetween(birthday,'2020-01-01','2020-12-31')")
public class BirthdayScopeRule extends DeclarativeRule {
    public BirthdayScopeRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
