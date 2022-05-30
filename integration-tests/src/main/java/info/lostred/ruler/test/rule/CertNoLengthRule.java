package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;

@Rule(ruleCode = "身份证号码长度",
        businessType = "person",
        description = "身份证号码长度必须为18位",
        parameterExp = "certNo",
        conditionExp = "certNo!=null",
        predicateExp = "certNo.length()!=18")
public class CertNoLengthRule extends AbstractRule {
    public CertNoLengthRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
