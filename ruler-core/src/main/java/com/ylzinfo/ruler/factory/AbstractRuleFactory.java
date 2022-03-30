package com.ylzinfo.ruler.factory;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 抽象规则工厂
 *
 * @author dengluwei
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
            throw new RuntimeException("Rule code '" + ruleInfo.getRuleCode() + "' is repeat.");
        }
        ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
    }

    @Override
    public void createRule(RuleInfo ruleInfo) {
        Class<?> validClass = ruleInfo.getValidClass();
        AbstractRule<?> rule = builder(validConfiguration, ruleInfo, validClass).build();
        this.rules.put(ruleInfo.getRuleCode(), rule);
    }

    @Override
    public ValidConfiguration getValidConfiguration() {
        return this.validConfiguration;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<AbstractRule<E>> getRules(String businessType, Class<E> validClass) {
        return this.rules.values().stream()
                .filter(e -> e.getRuleInfo().getBusinessType().equals(businessType)
                        && (e.getRuleInfo().getValidClass().equals(validClass)
                        || e.getRuleInfo().getValidClass().equals(Object.class)))
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

    /**
     * 获取规则的建造者
     *
     * @param validConfiguration 规则配置
     * @param ruleInfo           规则信息
     * @param validClass         规则约束类的类对象
     * @param <E>                规则约束的参数类型
     * @return 某个规则的建造者实例对象
     */
    public static <E> Builder<E> builder(ValidConfiguration validConfiguration, RuleInfo ruleInfo, Class<E> validClass) {
        return new Builder<>(validConfiguration, ruleInfo, validClass);
    }

    /**
     * 规则建造者
     */
    public static class Builder<E> {
        private final ValidConfiguration validConfiguration;
        private final RuleInfo ruleInfo;
        private final Class<E> validClass;

        public Builder(ValidConfiguration validConfiguration, RuleInfo ruleInfo, Class<E> validClass) {
            this.validConfiguration = validConfiguration;
            this.ruleInfo = ruleInfo;
            this.validClass = validClass;
        }

        @SuppressWarnings("unchecked")
        public AbstractRule<E> build() {
            try {
                if (!validClass.equals(ruleInfo.getValidClass())) {
                    throw new IllegalArgumentException("Rule info's 'ruleClassName' is different from the parameter passed in.");
                }
                Class<?> ruleClass = this.getClass().getClassLoader().loadClass(ruleInfo.getRuleClassName());
                Constructor<?> constructor = ruleClass.getDeclaredConstructor(ValidConfiguration.class, RuleInfo.class);
                Object object = constructor.newInstance(validConfiguration, ruleInfo);
                if (object instanceof AbstractRule<?>) {
                    return (AbstractRule<E>) object;
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Internal error: " + ruleInfo.getRuleClassName() + " cannot be instantiated.", e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Internal error: " + ruleInfo.getRuleClassName() + " cannot be instantiated, because it is not instance of Rule.");
        }
    }
}
