package com.ylzinfo.ruler.rule.extension;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.model.ValidClass;

@Rule(ruleCode = "test", businessType = "common", desc = "test", validClass = ValidClass.class)
public class TestRule extends AbstractRule<ValidClass> {

    public TestRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(ValidClass element) {
        return true;
    }

    @Override
    public boolean judge(ValidClass element) {
        return false;
    }

    @Override
    public Report buildReport(ValidClass element) {
        return this.getReport(ruleInfo, element, "test", "test");
    }
}
