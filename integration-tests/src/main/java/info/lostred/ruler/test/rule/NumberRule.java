package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.domain.model.ValidClass;

@Rule(ruleCode = "test_1", desc = "number必须>0", validClass = ValidClass.class)
public class NumberRule extends AbstractRule<ValidClass> {

    private final static String FIELD_NAME = "number";

    public NumberRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass element) {
        return element.getNumber() != null;
    }

    @Override
    public boolean judge(ValidClass element) {
        return element.getNumber().intValue() <= 0;
    }

    @Override
    public Report buildReport(ValidClass element) {
        if (this.judge(element)) {
            return this.wrapToReport(ruleInfo, element, FIELD_NAME, element.getNumber());
        }
        return null;
    }
}
