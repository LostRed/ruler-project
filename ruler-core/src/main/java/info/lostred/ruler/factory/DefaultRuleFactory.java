package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.ClassPathScanUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    public DefaultRuleFactory(String... scanPackages) {
        if (scanPackages == null || scanPackages.length == 0) {
            throw new IllegalArgumentException("At least one base package must be specified");
        }
        Arrays.stream(scanPackages)
                .flatMap(e -> ClassPathScanUtils.getClasses(e).stream())
                .filter(AbstractRule.class::isAssignableFrom)
                .filter(e -> e.isAnnotationPresent(Rule.class))
                .map(e -> RuleDefinitionBuilder.build(e).getRuleDefinition())
                .forEach(this::registerRuleDefinition);
    }

    public DefaultRuleFactory(Iterable<RuleDefinition> ruleDefinitions) {
        Map<String, RuleDefinition> ruleDefinitionMap = this.getRuleDefinitionMap();
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            ruleDefinitionMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
        }
    }
}
