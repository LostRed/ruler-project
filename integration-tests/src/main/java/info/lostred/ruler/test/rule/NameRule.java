package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.ProgrammaticRule;
import info.lostred.ruler.test.domain.Person;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.ObjectUtils;

@Rule(ruleCode = "姓名必填",
        businessType = "person",
        description = "姓名不能为空")
public class NameRule extends ProgrammaticRule<Person> {
    public NameRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    public Object getInitValue(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return person.getName();
    }

    @Override
    public boolean supports(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return !ObjectUtils.isEmpty(person);
    }

    @Override
    public boolean evaluate(EvaluationContext context, ExpressionParser parser) {
        Person person = this.getRootObject(context, parser);
        return ObjectUtils.isEmpty(person.getName());
    }
}
