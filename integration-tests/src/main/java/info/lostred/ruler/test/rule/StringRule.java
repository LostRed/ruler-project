package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.domain.model.ValidClass;

import java.util.Map;
import java.util.Set;

@Rule(ruleCode = "test_2", businessType = "test", desc = "number>0时，string不能为test", validClass = ValidClass.class)
public class StringRule extends AbstractRule<ValidClass> {

    private final static String FIELD_NAME = "string";

    public StringRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass object) {
        return object.getNumber() != null
                && object.getNumber().intValue() > 3;
    }

    @Override
    public boolean judge(ValidClass object) {
        return "test".equals(object.getString());
    }

    @Override
    public Report buildReport(ValidClass object) {
        if (this.judge(object)) {
            Set<Map.Entry<String, Object>> set = this.getEntry(FIELD_NAME, object.getString());
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
