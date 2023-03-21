package info.lostred.ruler.builder;

import info.lostred.ruler.engine.AbstractRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.exception.RulesEnginesException;
import info.lostred.ruler.factory.RuleFactory;
import org.springframework.expression.BeanResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 规则引擎建造者
 *
 * @author lostred
 * @since 3.2.0
 */
public class RulesEngineBuilder {
    private final AbstractRulesEngine abstractRulesEngine;

    public static RulesEngineBuilder build(Class<? extends AbstractRulesEngine> rulesEngineClass) {
        return new RulesEngineBuilder(rulesEngineClass);
    }

    private RulesEngineBuilder(Class<? extends AbstractRulesEngine> rulesEngineClass) {
        try {
            Constructor<? extends AbstractRulesEngine> constructor = rulesEngineClass.getDeclaredConstructor();
            abstractRulesEngine = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RulesEnginesException("Please provide a no argument constructor in " + rulesEngineClass.getName() +
                    ", override 'init()' method to initialize its member parameters.", e, rulesEngineClass);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RulesEnginesException("Internal error: " + rulesEngineClass.getName() +
                    " cannot be instantiated.", e, rulesEngineClass);
        }
    }

    /**
     * 设置业务类型
     *
     * @param businessType 业务类型
     * @return 规则引擎建造者
     */
    public RulesEngineBuilder businessType(String businessType) {
        abstractRulesEngine.setBusinessType(businessType);
        return this;
    }

    /**
     * 设置规则工厂
     *
     * @param ruleFactory 规则工厂
     * @return 规则引擎建造者
     */
    public RulesEngineBuilder ruleFactory(RuleFactory ruleFactory) {
        abstractRulesEngine.setRuleFactory(ruleFactory);
        return this;
    }

    /**
     * 设置bean解析器
     *
     * @param beanResolver bean解析器
     * @return 规则引擎建造者
     */
    public RulesEngineBuilder beanResolver(BeanResolver beanResolver) {
        abstractRulesEngine.setBeanResolver(beanResolver);
        return this;
    }

    /**
     * 设置全局函数
     *
     * @param globalFunctions 全局函数方法
     * @return 规则引擎建造者
     */
    public RulesEngineBuilder globalFunctions(List<Method> globalFunctions) {
        abstractRulesEngine.setGlobalFunctions(globalFunctions);
        return this;
    }

    /**
     * 实例化规则引擎
     *
     * @return 规则引擎实例
     */
    public RulesEngine getRulesEngine() {
        this.checkRulesEngine();
        abstractRulesEngine.reloadRules();
        return abstractRulesEngine;
    }

    /**
     * 检查规则引擎
     */
    private void checkRulesEngine() {
        String classname = abstractRulesEngine.getClass().getName();
        if (abstractRulesEngine.getBusinessType() == null) {
            throw new IllegalArgumentException(classname + " need to set a businessType in builder.");
        }
        if (abstractRulesEngine.getRuleFactory() == null) {
            throw new IllegalArgumentException(classname + " need to set a RuleFactory in builder.");
        }
        if (abstractRulesEngine.getBeanResolver() == null) {
            throw new IllegalArgumentException(classname + " need to set a BeanResolver in builder.");
        }
    }
}
