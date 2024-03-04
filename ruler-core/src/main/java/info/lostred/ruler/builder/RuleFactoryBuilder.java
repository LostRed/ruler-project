package info.lostred.ruler.builder;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.factory.RuleFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 规则工厂建造者
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleFactoryBuilder {
    private final RuleFactory ruleFactory;

    public static RuleFactoryBuilder build(Class<? extends RuleFactory> ruleFactoryClass) {
        return new RuleFactoryBuilder(ruleFactoryClass);
    }

    private RuleFactoryBuilder(Class<? extends RuleFactory> ruleFactoryClass) {
        try {
            Constructor<? extends RuleFactory> constructor = ruleFactoryClass.getDeclaredConstructor();
            ruleFactory = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RulesEnginesException("Please provide a no argument constructor in " + ruleFactoryClass.getName() +
                    ", override 'init()' method to initialize its member parameters.", e, ruleFactoryClass);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RulesEnginesException("Internal error: " + ruleFactoryClass.getName() +
                    " cannot be instantiated.", e, ruleFactoryClass);
        }
    }

    /**
     * 注册规则定义
     *
     * @param ruleDefinitions 规则定义集合
     * @return 规则工厂建造者
     */
    public RuleFactoryBuilder registerRuleDefinition(Iterable<RuleDefinition> ruleDefinitions) {
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            ruleFactory.registerRuleDefinition(ruleDefinition);
        }
        return this;
    }

    /**
     * 实例化规则工厂
     *
     * @return 规则工厂实例
     */
    public RuleFactory getRuleFactory() {
        return ruleFactory;
    }
}
