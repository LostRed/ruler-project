package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;

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
    public List<AbstractRule> getRulesWithBusinessType(String businessType) {
        Map<String, RuleDefinition> ruleDefinitionMap = this.getRuleDefinitionMap();
        return ruleDefinitionMap.values().stream()
                .filter(e -> e.getBusinessType().equals(businessType))
                .map(RuleDefinition::getRuleClass)
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
