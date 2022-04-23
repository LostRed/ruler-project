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

    public DefaultRuleFactory(ExpressionParser parser, String... scanPackages) {
        this.parser = parser;
        if (scanPackages != null) {
            this.registerFromPackages(scanPackages);
        }
    }

    /**
     * 从包中注册规则信息与规则
     *
     * @param packages 包名数组
     */
    private void registerFromPackages(String[] packages) {
        for (String packageName : packages) {
            PackageScanUtils.getClasses(packageName).stream()
                    .filter(AbstractRule.class::isAssignableFrom)
                    .filter(e -> e.isAnnotationPresent(Rule.class))
                    .map(this::buildRuleDefinition)
                    .forEach(this::register);
        }
        for (String ruleCode : this.ruleInfoMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleInfoMap.get(ruleCode);
            this.createRule(ruleDefinition, parser);
        }
    }

    /**
     * 构建规则定义
     *
     * @param ruleClass 规则类的类对象
     */
    private RuleDefinition buildRuleDefinition(Class<?> ruleClass) {
        Rule rule = ruleClass.getAnnotation(Rule.class);
        return RuleDefinition.of(rule, ruleClass);
    }
}
