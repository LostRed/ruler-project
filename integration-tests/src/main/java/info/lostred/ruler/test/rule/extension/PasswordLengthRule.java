package info.lostred.ruler.test.rule.extension;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;
import info.lostred.ruler.util.ReflectUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(ruleCode = "test2", businessType = "person", desc = "密码长度必须大于或等于6位", validClass = Contact.class)
public class PasswordLengthRule extends AbstractRule<Person> {

    public PasswordLengthRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(Person element) {
        return element.getContacts() != null && !element.getContacts().isEmpty();
    }

    @Override
    public boolean judge(Person element) {
        return element.getContacts().stream()
                .anyMatch(contact -> contact.getPassword().length() < 6);
    }

    @Override
    public Report buildReport(Person element) {
        Set<Map.Entry<String, Object>> set = element.getContacts().stream()
                .flatMap(e -> {
                    String nodeTrace = ReflectUtils.concatNodeTrace("contacts", e, "password");
                    return this.getEntry(nodeTrace, e.getPassword()).stream();
                })
                .collect(Collectors.toSet());
        if (!set.isEmpty()) {
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
