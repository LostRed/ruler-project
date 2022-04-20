package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.domain.model.ValidClass;

import java.util.Map;
import java.util.Set;

@Rule(ruleCode = "test_1", businessType = "test", desc = "number必须>0", validClass = ValidClass.class)
public class NumberRule extends AbstractRule<ValidClass> {

    private final static String FIELD_NAME = "number";

    public NumberRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass object) {
        return object.getNumber() != null;
    }

    @Override
    public boolean judge(ValidClass object) {
        return object.getNumber().intValue() <= 0;
    }

    @Override
    public Report buildReport(ValidClass object) {
        if (this.judge(object)) {
            Set<Map.Entry<String, Object>> set = this.getEntry(FIELD_NAME, object.getNumber());
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
