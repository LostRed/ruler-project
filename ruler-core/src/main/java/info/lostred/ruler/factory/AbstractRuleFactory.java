package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.builder.RuleBuilder;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 抽象规则工厂
 *
 * @author lostred
 */
public abstract class AbstractRuleFactory implements RuleFactory {
    /**
     * 表达式解析器
     */
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    /**
     * 规则定义缓存
     * <p>所有注册过的规则的规则定义都会被存放在这里</p>
     */
    private final Map<String, RuleDefinition> ruleDefinitionMap = new ConcurrentHashMap<>();
    /**
     * 规则缓存
     * <p>所有注册过的规则都会被存放在这里</p>
     */
    private final Map<String, AbstractRule> abstractRuleMap = new ConcurrentHashMap<>();

    @Override
    public void registerRuleDefinition(RuleDefinition ruleDefinition) {
        if (this.ruleDefinitionMap.containsKey(ruleDefinition.getRuleCode())) {
            throw new RulesException("Rule code '" + ruleDefinition.getRuleCode() + "' is repeat.", ruleDefinition);
        }
        this.ruleDefinitionMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
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
            AbstractRule proxyRule = this.createRule(ruleCode, ruleDefinition);
            abstractRuleMap.put(ruleCode, proxyRule);
            return proxyRule;
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
            return RuleBuilder.build(ruleDefinition)
                    .expressionParser(expressionParser)
                    .getProxyRule();
        }
    }

    @Override
    public List<AbstractRule> getRulesWithBusinessType(String businessType) {
        Map<String, RuleDefinition> ruleDefinitionMap = this.getRuleDefinitionMap();
        return ruleDefinitionMap.values().stream()
                .filter(e -> e.getBusinessType().equals(businessType))
                .map(RuleDefinition::getRuleCode)
                .map(this::getRule)
                .collect(Collectors.toList());
    }

    @Override
    public AbstractRule unregisterRule(String ruleCode) {
        this.getRuleDefinitionMap().remove(ruleCode);
        return this.getAbstractRuleMap().remove(ruleCode);
    }

    @Override
    public void unregisterAllRules() {
        this.getRuleDefinitionMap().clear();
        this.getAbstractRuleMap().clear();
    }

    public Map<String, RuleDefinition> getRuleDefinitionMap() {
        return ruleDefinitionMap;
    }

    public Map<String, AbstractRule> getAbstractRuleMap() {
        return abstractRuleMap;
    }
}
