package info.lostred.ruler.factory;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.AbstractRuleProxy;
import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.exception.RuleInitException;

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
    protected final GlobalConfiguration globalConfiguration;

    public AbstractRuleFactory(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    @Override
    public void registerRuleInfo(RuleInfo ruleInfo) {
        if (ruleInfoMap.containsKey(ruleInfo.getRuleCode())) {
            throw new RuleInitException("Rule code '" + ruleInfo.getRuleCode() + "' is repeat.", ruleInfo);
        }
        ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
    }

    @Override
    public void createRule(RuleInfo ruleInfo) {
        AbstractRule<?> rule = this.builder(globalConfiguration, ruleInfo).build();
        this.rules.put(ruleInfo.getRuleCode(), rule);
    }

    @Override
    public GlobalConfiguration getGlobalConfiguration() {
        return this.globalConfiguration;
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
     * @param globalConfiguration 规则配置
     * @param ruleInfo            规则信息
     * @param <E>                 规则约束的参数类型
     * @return 某个规则的建造者实例对象
     */
    public <E> Builder<E> builder(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        return new Builder<>(globalConfiguration, ruleInfo);
    }

    /**
     * 规则建造者
     */
    public static class Builder<E> {
        private final GlobalConfiguration globalConfiguration;
        private final RuleInfo ruleInfo;

        public Builder(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
            this.globalConfiguration = globalConfiguration;
            this.ruleInfo = ruleInfo;
        }

        @SuppressWarnings("unchecked")
        public AbstractRule<E> build() {
            try {
                Class<?> ruleClass = this.getClass().getClassLoader().loadClass(ruleInfo.getRuleClassName());
                Constructor<?> constructor = ruleClass.getDeclaredConstructor(GlobalConfiguration.class, RuleInfo.class);
                Object object = constructor.newInstance(globalConfiguration, ruleInfo);
                if (object instanceof AbstractRule<?>) {
                    //创建代理器
                    AbstractRuleProxy proxy = new AbstractRuleProxy((AbstractRule<E>) object);
                    //拿到代理对象
                    return proxy.newProxyInstance();
                }
                throw new RuleInitException("Internal error: " + ruleInfo.getRuleClassName() +
                        " cannot be instantiated, because it is not instance of Rule.", this.ruleInfo);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | ClassNotFoundException e) {
                throw new RuleInitException("Internal error: " + ruleInfo.getRuleClassName() +
                        " cannot be instantiated.", e, this.ruleInfo);
            }
        }
    }
}
