package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleBuilder;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.ClassPathScanUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.Map;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    private final ExpressionParser expressionParser = new SpelExpressionParser();

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

    @Override
    public AbstractRule getRule(String ruleCode) {
        Map<String, AbstractRule> abstractRuleMap = this.getAbstractRuleMap();
        if (abstractRuleMap.containsKey(ruleCode)) {
            return abstractRuleMap.get(ruleCode);
        } else {
            RuleDefinition ruleDefinition = this.getRuleDefinitionMap().get(ruleCode);
            if (ruleDefinition == null) {
                throw new RuntimeException("The rule [" + ruleCode + "] is not found in RuleFactory");
            }
            return this.createRule(ruleCode, ruleDefinition);
        }
    }

    @Override
    public AbstractRule getRule(Class<? extends AbstractRule> ruleClass) {
        Rule rule = ruleClass.getAnnotation(Rule.class);
        if (rule == null) {
            throw new IllegalArgumentException("@Rule is missing on the type of '" + ruleClass.getName() + "'");
        }
        String ruleCode = rule.ruleCode();
        return this.getRule(ruleCode);
    }

    /**
     * 创建规则
     *
     * @param ruleCode       规则编号
     * @param ruleDefinition 规则定义
     * @return 抽象规则
     */
    protected synchronized AbstractRule createRule(String ruleCode, RuleDefinition ruleDefinition) {
        Map<String, AbstractRule> abstractRuleMap = this.getAbstractRuleMap();
        if (abstractRuleMap.containsKey(ruleCode)) {
            return abstractRuleMap.get(ruleCode);
        } else {
            AbstractRule proxyRule = RuleBuilder.build(ruleDefinition)
                    .expressionParser(expressionParser)
                    .getProxyRule();
            this.getAbstractRuleMap().put(ruleCode, proxyRule);
            return proxyRule;
        }
    }
}
