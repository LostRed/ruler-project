package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;
import info.lostred.ruler.test.domain.Contact;
import info.lostred.ruler.test.domain.Person;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "联系方式密码不能为1234")
public class ContactRule extends ProgrammaticRule<Person> {
    public ContactRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    public Object getInitValue(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return person.getContacts().stream()
                .map(Contact::getPassword)
                .filter("1234"::equals)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return !ObjectUtils.isEmpty(person.getContacts());
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return person.getContacts().stream()
                .map(Contact::getPassword)
                .anyMatch("1234"::equals);
    }
}
