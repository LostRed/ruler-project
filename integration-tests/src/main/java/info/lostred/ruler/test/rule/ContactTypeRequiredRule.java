package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.SpELRule;

@Rule(ruleCode = "联系方式类型必填",
        businessType = "person",
        description = "联系方式中的类型必须填写",
        parameterExp = "contacts[#i].type",
        conditionExp = "true",
        predicateExp = "contacts[#i].type==null||contacts[#i].type.isEmpty()")
public class ContactTypeRequiredRule extends SpELRule {
    public ContactTypeRequiredRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
