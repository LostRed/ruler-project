package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.DeclarativeRule;

@Rule(ruleCode = "地区国家必填",
        businessType = "person",
        description = "地区中的国家必须填写",
        parameterExp = "area.country",
        conditionExp = "true",
        predicateExp = "area.country==null||area.country.isEmpty()")
public class AreaCountryRequiredRule extends DeclarativeRule {
    public AreaCountryRequiredRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
