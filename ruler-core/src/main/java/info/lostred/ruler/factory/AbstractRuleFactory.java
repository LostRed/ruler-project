package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.proxy.RuleProxy;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
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
    /**
     * 规则定义缓存
     * <p>所有注册过的规则的规则定义都会被存放在这里</p>
     */
    protected final Map<String, RuleDefinition> ruleDefinitionMap = new ConcurrentHashMap<>();
    /**
     * 规则缓存
     * <p>所有注册过的规则都会被存放在这里</p>
     */
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
    public AbstractRule createRule(RuleDefinition ruleDefinition, ExpressionParser expressionParser, BeanResolver beanResolver) {
        return this.builder(ruleDefinition)
                .expressionParser(expressionParser)
                .beanResolver(beanResolver)
                .build();
    }

    @Override
    public void destroyAllRules() {
        this.ruleDefinitionMap.clear();
        this.rules.clear();
    }

    @Override
    public AbstractRule destroyRule(String ruleCode) {
        this.ruleDefinitionMap.remove(ruleCode);
        return this.rules.remove(ruleCode);
    }

    @Override
    public AbstractRule getRule(String ruleCode) {
        return this.rules.get(ruleCode);
    }

    @Override
    public AbstractRule getRule(Class<? extends AbstractRule> ruleClass) {
        for (AbstractRule abstractRule : rules.values()) {
            if (abstractRule.getClass().equals(ruleClass)) {
                return abstractRule;
            }
        }
        throw new IllegalArgumentException("there is not type of '" + ruleClass.getName() + "' rule");
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
     * @return 某个规则的建造者实例对象
     */
    public Builder builder(RuleDefinition ruleDefinition) {
        return new Builder(ruleDefinition);
    }

    /**
     * 规则建造者
     */
    private static class Builder {
        private final AbstractRule abstractRule;

        private Builder(RuleDefinition ruleDefinition) {
            Class<?> ruleClass = ruleDefinition.getRuleClass();
            try {
                Constructor<?> constructor = ruleClass.getDeclaredConstructor();
                Object object = constructor.newInstance();
                if (object instanceof AbstractRule) {
                    ((AbstractRule) object).setRuleDefinition(ruleDefinition);
                    //创建代理器
                    RuleProxy proxy = new RuleProxy((AbstractRule) object);
                    //拿到代理对象
                    this.abstractRule = proxy.newProxyInstance();
                } else {
                    throw new RulesException("Internal error: " + ruleClass.getName() +
                            " cannot be instantiated, because it is not instance of AbstractRule.", ruleDefinition);
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RulesException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated.", e, ruleDefinition);
            }
        }

        public Builder expressionParser(ExpressionParser expressionParser) {
            this.abstractRule.setExpressionParser(expressionParser);
            return this;
        }

        public Builder beanResolver(BeanResolver beanResolver) {
            this.abstractRule.setBeanResolver(beanResolver);
            return this;
        }

        public AbstractRule build() {
            this.abstractRule.init();
            return this.abstractRule;
        }
    }
}
