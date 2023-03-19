package info.lostred.ruler.builder;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;

/**
 * 规则定义建造者
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleDefinitionBuilder {
    private final RuleDefinition ruleDefinition;

    public static RuleDefinitionBuilder build(Class<?> ruleClass) {
        return new RuleDefinitionBuilder(ruleClass);
    }

    @SuppressWarnings("unchecked")
    private RuleDefinitionBuilder(Class<?> ruleClass) {
        if (!AbstractRule.class.isAssignableFrom(ruleClass)) {
            throw new IllegalArgumentException("Class '" + ruleClass.getName() + "' is not a AbstractRule.");
        }
        Rule rule = ruleClass.getAnnotation(Rule.class);
        ruleDefinition = RuleDefinition.of(rule, (Class<? extends AbstractRule>) ruleClass);
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
