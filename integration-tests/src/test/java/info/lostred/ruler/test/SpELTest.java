package info.lostred.ruler.test;

import info.lostred.ruler.test.domain.Contact;
import info.lostred.ruler.test.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class SpELTest {
    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Person person = new Person();
        Expression expression = parser.parseExpression("#person.contacts.add(new info.lostred.ruler.test.domain.Contact())");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("person", person);
        Object value = expression.getValue(context);
        System.out.println(value);
    }

    @Test
    public void test2() {
        ExpressionParser parser = new SpelExpressionParser();
        Person person = new Person();
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setType("qq");
        Contact contact2 = new Contact();
        contact2.setType("weixin");
        contacts.add(contact1);
        contacts.add(contact2);
        person.setContacts(contacts);
        EvaluationContext context = new StandardEvaluationContext(person);
        Expression expression = parser.parseExpression("contacts.?[type.equals('qq')].size()==1");
        Boolean value = expression.getValue(context, Boolean.class);
        System.out.println(value);
    }
}
