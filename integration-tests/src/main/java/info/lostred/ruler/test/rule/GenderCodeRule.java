package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.rule.DeclarativeRule;

@Rule(ruleCode = "性别码值",
        businessType = "person",
        grade = Grade.SUSPECTED,
        description = "性别必须是字典值",
        parameterExp = "gender",
        conditionExp = "gender!=null",
        predicateExp = "!@dict.get('gender').contains(gender)")
public class GenderCodeRule extends DeclarativeRule {
}
