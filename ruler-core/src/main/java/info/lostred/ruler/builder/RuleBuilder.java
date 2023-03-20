package info.lostred.ruler.builder;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.DeclarativeRule;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 规则建造者
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleBuilder {
    private final static ExpressionParser PARSER = new SpelExpressionParser();
    private final AbstractRule abstractRule;

    public static RuleBuilder build(RuleDefinition ruleDefinition) {
        return new RuleBuilder(ruleDefinition);
    }

    private RuleBuilder(RuleDefinition ruleDefinition) {
        Class<?> ruleClass = ruleDefinition.getRuleClass();
        try {
            Constructor<?> constructor = ruleClass.getDeclaredConstructor();
            Object object = constructor.newInstance();
            if (object instanceof AbstractRule) {
                abstractRule = (AbstractRule) object;
                abstractRule.setRuleDefinition(ruleDefinition);
                if (abstractRule instanceof DeclarativeRule) {
                    ((DeclarativeRule) abstractRule).setExpressionParser(PARSER);
                }
                abstractRule.init();
            } else {
                throw new RulesException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated, because it is not instance of AbstractRule.", ruleDefinition);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RulesException("Internal error: " + ruleClass.getName() +
                    " cannot be instantiated.", e, ruleDefinition);
        }
    }

    public AbstractRule getAbstractRule() {
        this.checkRule();
        return abstractRule;
    }

    private void checkRule() {
        if (abstractRule instanceof DeclarativeRule) {
            DeclarativeRule declarativeRule = (DeclarativeRule) abstractRule;
            String classname = declarativeRule.getClass().getName();
            if (declarativeRule.getExpressionParser() == null) {
                throw new IllegalArgumentException(classname + " need to set an ExpressionParser in builder.");
            }
        }
    }
}
