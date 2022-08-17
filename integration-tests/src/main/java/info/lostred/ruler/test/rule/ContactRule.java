package info.lostred.ruler.test.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.GenericRule;
import info.lostred.ruler.test.domain.Contact;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.List;

@Rule(ruleCode = "联系方式",
        businessType = "person",
        description = "测试",
        parameterExp = "contacts")
public class ContactRule extends GenericRule<List<Contact>> {
    public ContactRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }

    @Override
    protected boolean doJudge(EvaluationContext context, ExpressionParser parser, List<Contact> value) {
        return value.stream().anyMatch(e -> e.getPassword().equals("1234"));
    }
}
