package info.lostred.ruler.test.rule.extension;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.test.entity.Person;

import java.util.Map;
import java.util.Set;

@Rule(ruleCode = "test1", businessType = "person", desc = "身份证长度必须为18位", validClass = Person.class)
public class CertNoLengthRule extends AbstractRule<Person> {

    public CertNoLengthRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(Person object) {
        return object.getCertNo() != null && !"".equals(object.getCertNo());
    }

    @Override
    public boolean judge(Person object) {
        return object.getCertNo().length() != 18;
    }

    @Override
    public Report buildReport(Person object) {
        if (this.judge(object)) {
            Set<Map.Entry<String, Object>> set = this.getEntry("certNo", object.getCertNo());
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
