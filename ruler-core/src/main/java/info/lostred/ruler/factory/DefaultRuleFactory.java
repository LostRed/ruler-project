package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.RuleDefinitionBuilder;
import info.lostred.ruler.util.ClassPathScanUtils;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    public DefaultRuleFactory(Iterable<AbstractRule> abstractRules) {
        for (AbstractRule abstractRule : abstractRules) {
            this.registerRule(abstractRule);
        }
    }

    public DefaultRuleFactory(ExpressionParser expressionParser, BeanResolver beanResolver, String... scanPackages) {
        this.registerRules(expressionParser, beanResolver, scanPackages);
    }

    /**
     * 从包中注册规则定义，并创建规则
     *
     * @param expressionParser 表达式解析器
     * @param beanResolver     bean解析器
     * @param scanPackages     规则类所在的包名
     */
    public void registerRules(ExpressionParser expressionParser, BeanResolver beanResolver, String... scanPackages) {
        if (scanPackages == null || scanPackages.length == 0) {
            throw new IllegalArgumentException("At least one base package must be specified");
        }
        //注册规则定义
        for (String packageName : scanPackages) {
            ClassPathScanUtils.getClasses(packageName).stream()
                    .filter(AbstractRule.class::isAssignableFrom)
                    .filter(e -> e.isAnnotationPresent(Rule.class))
                    .map(e -> RuleDefinitionBuilder.of(e).getRuleDefinition())
                    .forEach(this::registerRuleDefinition);
        }
        //创建并注册规则
        for (String ruleCode : this.ruleDefinitionMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleDefinitionMap.get(ruleCode);
            AbstractRule rule = this.createRule(ruleDefinition, expressionParser, beanResolver);
            this.rules.put(ruleDefinition.getRuleCode(), rule);
        }
    }
}
