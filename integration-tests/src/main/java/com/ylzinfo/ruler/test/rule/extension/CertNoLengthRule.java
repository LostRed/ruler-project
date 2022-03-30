package com.ylzinfo.ruler.test.rule.extension;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.test.entity.Person;

@Rule(ruleCode = "test", businessType = "person", desc = "身份证长度必须为18位", validClass = Person.class)
public class CertNoLengthRule extends AbstractRule<Person> {

    public CertNoLengthRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(Person element) {
        return element.getCertNo() != null && !"".equals(element.getCertNo());
    }

    @Override
    public boolean judge(Person element) {
        return element.getCertNo().length() != 18;
    }

    @Override
    public Report buildReport(Person element) {
        if (this.judge(element)) {
            return this.getReport(ruleInfo, element, "certNo", element.getCertNo());
        }
        return null;
    }
}
