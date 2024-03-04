package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.DeclarativeRule;
import info.lostred.ruler.util.ClassUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    private final static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    public DefaultRuleFactory(String... scanPackages) {
        if (scanPackages == null || scanPackages.length == 0) {
            throw new IllegalArgumentException("At least one base package must be specified");
        }
        for (String scanPackage : scanPackages) {
            ClassUtils.getTypeFromPackage(scanPackage).stream()
                    .filter(e -> e.isAnnotationPresent(Rule.class))
                    .map(e -> RuleDefinitionBuilder.build(e).getRuleDefinition())
                    .forEach(this::registerRuleDefinition);
        }
    }

    protected AbstractRule createRule(String ruleCode, RuleDefinition ruleDefinition) {
        Class<? extends AbstractRule> ruleClass = ruleDefinition.getRuleClass();
        try {
            Constructor<? extends AbstractRule> constructor = ruleClass.getDeclaredConstructor();
            AbstractRule abstractRule = constructor.newInstance();
            abstractRule.setRuleDefinition(ruleDefinition);
            if (abstractRule instanceof DeclarativeRule) {
                ((DeclarativeRule) abstractRule).setExpressionParser(EXPRESSION_PARSER);
            }
            abstractRule.init();
            return abstractRule;
        } catch (NoSuchMethodException e) {
            throw new RulesException("Please provide a no argument constructor in " + ruleClass.getName() +
                    ", override 'init()' method to initialize its member parameters.", e, ruleDefinition);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RulesException("Internal error: " + ruleClass.getName() +
                    " cannot be instantiated.", e, ruleDefinition);
        }
    }
}
