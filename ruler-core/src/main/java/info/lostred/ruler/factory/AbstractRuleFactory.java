package info.lostred.ruler.factory;

import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.proxy.AbstractRuleProxy;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.exception.RuleInitializationException;
import info.lostred.ruler.rule.SingleFieldRule;

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
    protected final Map<String, RuleInfo> ruleInfoMap = new ConcurrentHashMap<>();
    protected final Map<String, AbstractRule<?>> rules = new ConcurrentHashMap<>();
    protected final ValidConfiguration validConfiguration;

    public AbstractRuleFactory(ValidConfiguration validConfiguration) {
        this.validConfiguration = validConfiguration;
    }

    @Override
    public void registerRuleInfo(RuleInfo ruleInfo) {
        if (ruleInfoMap.containsKey(ruleInfo.getRuleCode())) {
            throw new RuleInitializationException("Rule code '" + ruleInfo.getRuleCode() + "' is repeat.", ruleInfo);
        }
        ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
    }

    @Override
    public void registerRule(AbstractRule<?> abstractRule) {
        RuleInfo ruleInfo = abstractRule.getRuleInfo();
        ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
        rules.put(ruleInfo.getRuleCode(), abstractRule);
    }

    @Override
    public void createRule(RuleInfo ruleInfo) {
        AbstractRule<?> rule = this.builder(validConfiguration, ruleInfo).build();
        this.rules.put(ruleInfo.getRuleCode(), rule);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<AbstractRule<E>> findRules(String businessType) {
        return this.rules.values().stream()
                .filter(e -> e.getRuleInfo().getBusinessType().equals(businessType))
                .map(e -> (AbstractRule<E>) e)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> AbstractRule<E> getRule(String ruleCode) {
        if (!this.ruleInfoMap.containsKey(ruleCode)) {
            throw new RuntimeException("This rule didn't register.");
        }
        AbstractRule<?> rule = this.rules.get(ruleCode);
        return (AbstractRule<E>) rule;
    }

    @Override
    public ValidConfiguration getValidConfiguration() {
        return validConfiguration;
    }

    /**
     * 获取规则的建造者
     *
     * @param validConfiguration 规则配置
     * @param ruleInfo           规则信息
     * @param <E>                规则约束的参数类型
     * @return 某个规则的建造者实例对象
     */
    public <E> Builder<E> builder(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        return new Builder<>(validConfiguration, ruleInfo);
    }

    /**
     * 规则建造者
     */
    private static class Builder<E> {
        private final ValidConfiguration validConfiguration;
        private final RuleInfo ruleInfo;

        private Builder(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
            this.validConfiguration = validConfiguration;
            this.ruleInfo = ruleInfo;
        }

        @SuppressWarnings("unchecked")
        public AbstractRule<E> build() {
            try {
                Class<?> ruleClass = this.getClass().getClassLoader().loadClass(ruleInfo.getRuleClassName());
                Object object;
                if (SingleFieldRule.class.isAssignableFrom(ruleClass)) {
                    Constructor<?> constructor = ruleClass.getDeclaredConstructor(RuleInfo.class, ValidConfiguration.class);
                    object = constructor.newInstance(ruleInfo, validConfiguration);
                } else {
                    Constructor<?> constructor = ruleClass.getDeclaredConstructor(RuleInfo.class);
                    object = constructor.newInstance(ruleInfo);
                }
                if (object instanceof AbstractRule<?>) {
                    //创建代理器
                    AbstractRuleProxy proxy = new AbstractRuleProxy((AbstractRule<E>) object);
                    //拿到代理对象
                    return proxy.newProxyInstance();
                }
                throw new RuleInitializationException("Internal error: " + ruleInfo.getRuleClassName() +
                        " cannot be instantiated, because it is not instance of Rule.", this.ruleInfo);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | ClassNotFoundException e) {
                throw new RuleInitializationException("Internal error: " + ruleInfo.getRuleClassName() +
                        " cannot be instantiated.", e, this.ruleInfo);
            }
        }
    }
}
