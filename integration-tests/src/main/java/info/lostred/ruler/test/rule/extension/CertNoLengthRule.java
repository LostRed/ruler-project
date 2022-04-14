package info.lostred.ruler.test.rule.extension;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.entity.Person;

@Rule(ruleCode = "test", businessType = "person", desc = "身份证长度必须为18位", validClass = Person.class)
public class CertNoLengthRule extends AbstractRule<Person> {

    public CertNoLengthRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
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
            return this.wrapToReport(ruleInfo, element, "certNo", element.getCertNo());
        }
        return null;
    }
}
