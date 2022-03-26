package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.model.ValidClass;

@Rule(ruleCode = "test_2", businessType = "common", desc = "number>0时，string不能为test", validClass = ValidClass.class)
public class StringRule extends AbstractRule<ValidClass> {

    private final static String FIELD_NAME = "string";

    public StringRule(ValidConfiguration config, RuleInfo ruleInfo) {
        super(config, ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass element) {
        return element.getNumber() != null
                && element.getNumber().intValue() > 3;
    }

    @Override
    public boolean judge(ValidClass element) {
        return "test".equals(element.getString());
    }

    @Override
    public Report buildReport(ValidClass element) {
        if (this.judge(element)) {
            return this.getReport(ruleInfo, element, FIELD_NAME, element.getString());
        }
        return null;
    }
}
