package info.lostred.ruler.test.rule.extension;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.entity.Contact;
import info.lostred.ruler.test.entity.Person;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(ruleCode = "test2", businessType = "person", desc = "密码长度必须大于或等于6位", validClass = Contact.class)
public class PasswordLengthRule extends AbstractRule<Person> {

    public PasswordLengthRule(RuleInfo ruleInfo) {
        super(ruleInfo);
    }

    @Override
    public boolean isSupported(Person object) {
        return object.getContacts() != null && !object.getContacts().isEmpty();
    }

    @Override
    public boolean judge(Person object) {
        return object.getContacts().stream()
                .anyMatch(contact -> contact.getPassword().length() < 6);
    }

    @Override
    public Report buildReport(Person object) {
        Set<Map.Entry<String, Object>> set = object.getContacts().stream()
                .filter(contact -> contact.getPassword() != null && contact.getPassword().length() < 6)
                .flatMap(e -> this.getEntryFromCollection("contacts", e, "password", e.getPassword()).stream())
                .collect(Collectors.toSet());
        if (!set.isEmpty()) {
            return Report.of(ruleInfo).putIllegals(set);
        }
        return null;
    }
}
