package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.PackageScanUtils;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
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
            throw new IllegalArgumentException("scanPackages cannot be null or empty.");
        }
        //注册规则定义
        for (String packageName : scanPackages) {
            PackageScanUtils.getClasses(packageName).stream()
                    .filter(AbstractRule.class::isAssignableFrom)
                    .filter(e -> e.isAnnotationPresent(Rule.class))
                    .map(this::buildRuleDefinition)
                    .forEach(this::registerRuleDefinition);
        }
        //创建并注册规则
        for (String ruleCode : this.ruleDefinitionMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleDefinitionMap.get(ruleCode);
            AbstractRule rule = this.createRule(ruleDefinition, expressionParser, beanResolver);
            this.rules.put(ruleDefinition.getRuleCode(), rule);
        }
    }

    /**
     * 构建规则定义
     *
     * @param ruleClass 规则定义
     * @return 规则定义
     */
    @SuppressWarnings("unchecked")
    protected RuleDefinition buildRuleDefinition(Class<?> ruleClass) {
        if (!AbstractRule.class.isAssignableFrom(ruleClass)) {
            throw new IllegalArgumentException("Class '" + ruleClass.getName() + "' is not a AbstractRule.");
        }
        Rule rule = ruleClass.getAnnotation(Rule.class);
        return RuleDefinition.of(rule, (Class<? extends AbstractRule>) ruleClass);
    }
}
