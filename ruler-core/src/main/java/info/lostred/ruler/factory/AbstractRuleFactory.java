package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.proxy.RuleProxy;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    protected final Map<String, RuleDefinition> ruleDefinitionMap = new ConcurrentHashMap<>();
    protected final Map<String, AbstractRule> rules = new ConcurrentHashMap<>();

    @Override
    public void registerRuleDefinition(RuleDefinition ruleDefinition) {
        if (this.ruleDefinitionMap.containsKey(ruleDefinition.getRuleCode())) {
            throw new RulesException("Rule code '" + ruleDefinition.getRuleCode() + "' is repeat.", ruleDefinition);
        }
        this.ruleDefinitionMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
    }

    @Override
    public void registerRule(AbstractRule rule) {
        RuleDefinition ruleDefinition = rule.getRuleDefinition();
        this.ruleDefinitionMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
        this.rules.put(ruleDefinition.getRuleCode(), rule);
    }

    @Override
    public void createRule(RuleDefinition ruleDefinition, ExpressionParser parser) {
        AbstractRule rule = this.builder(ruleDefinition, parser).build();
        this.rules.put(ruleDefinition.getRuleCode(), rule);
    }

    @Override
    public void destroyRule(String ruleCode) {
        this.ruleDefinitionMap.remove(ruleCode);
        this.rules.remove(ruleCode);
    }

    @Override
    public AbstractRule getRule(String ruleCode) {
        if (!this.ruleDefinitionMap.containsKey(ruleCode)) {
            throw new IllegalArgumentException("This rule had not registered.");
        }
        return this.rules.get(ruleCode);
    }

    @Override
    public List<AbstractRule> findRules(String businessType) {
        return this.rules.values().stream()
                .filter(e -> e.getRuleDefinition().getBusinessType().equals(businessType))
                .collect(Collectors.toList());
    }

    @Override
    public List<RuleDefinition> getRuleDefinitions() {
        return this.rules.values().stream()
                .map(AbstractRule::getRuleDefinition)
                .collect(Collectors.toList());
    }

    /**
     * 获取规则的建造者
     *
     * @param ruleDefinition 规则定义
     * @param parser         表达式解析器
     * @return 某个规则的建造者实例对象
     */
    public Builder builder(RuleDefinition ruleDefinition, ExpressionParser parser) {
        return new Builder(ruleDefinition, parser);
    }

    /**
     * 规则建造者
     */
    private static class Builder {
        private final RuleDefinition ruleDefinition;
        private final ExpressionParser parser;

        private Builder(RuleDefinition ruleDefinition, ExpressionParser parser) {
            this.ruleDefinition = ruleDefinition;
            this.parser = parser;
        }

        public AbstractRule build() {
            Class<?> ruleClass = ruleDefinition.getRuleClass();
            try {
                Constructor<?> constructor = ruleClass.getDeclaredConstructor(RuleDefinition.class, ExpressionParser.class);
                Object object = constructor.newInstance(ruleDefinition, parser);
                if (object instanceof AbstractRule) {
                    //创建代理器
                    RuleProxy proxy = new RuleProxy((AbstractRule) object);
                    //拿到代理对象
                    return proxy.newProxyInstance();
                }
                throw new RulesException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated, because it is not instance of Rule.", this.ruleDefinition);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RulesException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated.", e, this.ruleDefinition);
            }
        }
    }
}
