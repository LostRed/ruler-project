package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.DeclarativeRule;

@Rule(ruleCode = "联系方式类型必填",
        businessType = "person",
        description = "联系方式中的类型必须填写",
        parameterExp = "contacts?.?[type==null||''.equals(type)].![type]",
        conditionExp = "true",
        predicateExp = "contacts?.?[type==null||''.equals(type)].size()>0")
public class ContactTypeRequiredRule extends DeclarativeRule {
    public ContactTypeRequiredRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
