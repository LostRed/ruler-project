package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.model.ValidClass;

public class NumberRule extends Rule<ValidClass> {

    private final static String FIELD_NAME = "number";

    public NumberRule(ValidConfiguration config, RuleInfo ruleInfo) {
        super(config, ruleInfo);
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
            return this.getReport(ruleInfo, element, FIELD_NAME, element.getNumber());
        }
        return null;
    }
}
