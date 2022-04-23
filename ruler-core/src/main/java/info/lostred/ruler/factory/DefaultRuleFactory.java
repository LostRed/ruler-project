package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.PackageScanUtils;
import org.springframework.expression.ExpressionParser;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    private final ExpressionParser parser;
    private final String[] scanPackages;

    public DefaultRuleFactory(ExpressionParser parser, String... scanPackages) {
        this.parser = parser;
        this.scanPackages = scanPackages;
        this.registerFromPackages();
    }

    /**
     * 从包中注册规则信息与规则
     */
    private void registerFromPackages() {
        if (scanPackages == null || scanPackages.length == 0) {
            return;
        }
        for (String packageName : scanPackages) {
            PackageScanUtils.getClasses(packageName).stream()
                    .filter(AbstractRule.class::isAssignableFrom)
                    .filter(e -> e.isAnnotationPresent(Rule.class))
                    .map(this::buildRuleDefinition)
                    .forEach(this::registerRuleDefinition);
        }
        for (String ruleCode : this.ruleDefinitionMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleDefinitionMap.get(ruleCode);
            this.createRule(ruleDefinition, parser);
        }
    }

    @SuppressWarnings("unchecked")
    private RuleDefinition buildRuleDefinition(Class<?> ruleClass) {
        if (!AbstractRule.class.isAssignableFrom(ruleClass)) {
            throw new IllegalArgumentException("Class '" + ruleClass.getName() + "' is not a AbstractRule.");
        }
        Rule rule = ruleClass.getAnnotation(Rule.class);
        return RuleDefinition.of(rule, (Class<? extends AbstractRule>) ruleClass);
    }
}
