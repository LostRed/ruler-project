package info.lostred.ruler.test.rule.extension;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.entity.Person;

import java.util.Map;
import java.util.Set;

@Rule(ruleCode = "test1", businessType = "person", desc = "身份证长度必须为18位", validClass = Person.class)
public class CertNoLengthRule extends AbstractRule<Person> {

    public CertNoLengthRule(RuleInfo ruleInfo) {
        super(ruleInfo);
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
            Set<Map.Entry<String, Object>> set = this.getEntry("certNo", element.getCertNo());
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
