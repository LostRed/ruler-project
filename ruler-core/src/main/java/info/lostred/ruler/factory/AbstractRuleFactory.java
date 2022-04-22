package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RuleInitializationException;
import info.lostred.ruler.proxy.DefaultRuleProxy;
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
    protected final Map<String, RuleDefinition> ruleInfoMap = new ConcurrentHashMap<>();
    protected final Map<String, AbstractRule> rules = new ConcurrentHashMap<>();

    @Override
    public void register(RuleDefinition ruleDefinition) {
        if (ruleInfoMap.containsKey(ruleDefinition.getRuleCode())) {
            throw new RuleInitializationException("Rule code '" + ruleDefinition.getRuleCode() + "' is repeat.", ruleDefinition);
        }
        ruleInfoMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
    }

    @Override
    public void register(AbstractRule rule) {
        RuleDefinition ruleDefinition = rule.getRuleDefinition();
        ruleInfoMap.put(ruleDefinition.getRuleCode(), ruleDefinition);
        rules.put(ruleDefinition.getRuleCode(), rule);
    }

    @Override
    public void createRule(RuleDefinition ruleDefinition, ExpressionParser parser) {
        AbstractRule rule = this.builder(ruleDefinition, parser).build();
        this.rules.put(ruleDefinition.getRuleCode(), rule);
    }

    @Override
    public List<AbstractRule> findRules(String businessType) {
        return this.rules.values().stream()
                .filter(e -> e.getRuleDefinition().getBusinessType().equals(businessType))
                .collect(Collectors.toList());
    }

    @Override
    public AbstractRule getRule(String ruleCode) {
        if (!this.ruleInfoMap.containsKey(ruleCode)) {
            throw new IllegalArgumentException("This rule didn't register.");
        }
        return this.rules.get(ruleCode);
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
                    DefaultRuleProxy proxy = new DefaultRuleProxy((AbstractRule) object);
                    //拿到代理对象
                    return proxy.newProxyInstance();
                }
                throw new RuleInitializationException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated, because it is not instance of Rule.", this.ruleDefinition);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuleInitializationException("Internal error: " + ruleClass.getName() +
                        " cannot be instantiated.", e, this.ruleDefinition);
            }
        }
    }
}
