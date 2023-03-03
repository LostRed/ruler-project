package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.rule.ProgrammaticRule;
import info.lostred.ruler.test.domain.Contact;
import info.lostred.ruler.test.domain.Person;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "联系方式密码不能为1234")
public class ContactRule extends ProgrammaticRule<Person> {
    @Override
    public Object getValueInternal(Person person) {
        return person.getContacts().stream()
                .map(Contact::getPassword)
                .filter("1234"::equals)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supportsInternal(Person person) {
        return !ObjectUtils.isEmpty(person.getContacts());
    }

    @Override
    public boolean evaluateInternal(Person person) {
        return person.getContacts().stream()
                .map(Contact::getPassword)
                .anyMatch("1234"::equals);
    }
}
