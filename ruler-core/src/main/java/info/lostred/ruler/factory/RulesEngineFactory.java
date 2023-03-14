package info.lostred.ruler.factory;

import info.lostred.ruler.engine.AbstractRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import org.springframework.expression.BeanResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 规则引擎工厂
 *
 * @author lostred
 */
public interface RulesEngineFactory {
    /**
     * 获取所有引擎的业务类型
     *
     * @return 业务类型集合
     */
    List<String> getAllEngineBusinessType();

    /**
     * 所有引擎从规则工厂中重新加载规则
     */
    void reloadRules();

    /**
     * 从引擎单例池中获取规则引擎
     *
     * @param businessType 业务类型
     * @return 规则引擎
     */
    RulesEngine getEngine(String businessType);

    /**
     * 获取所有规则引擎
     *
     * @return 规则引擎集合
     */
    Collection<? extends RulesEngine> getAllEngines();

    /**
     * 获取工厂的建造者对象
     *
     * @param rulesEngineClass 规则引擎实现类
     * @param <T>              规则引擎类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    static <T extends AbstractRulesEngine> Builder<T> builder(Class<T> rulesEngineClass) {
        return new Builder<>(rulesEngineClass);
    }

    /**
     * 规则引擎建造者
     *
     * @param <T> 规则引擎类型
     */
    class Builder<T extends AbstractRulesEngine> {
        private final T abstractRulesEngine;

        public Builder(Class<T> rulesEngineClass) {
            try {
                Constructor<T> constructor = rulesEngineClass.getDeclaredConstructor();
                abstractRulesEngine = constructor.newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * 设置业务类型
         *
         * @param businessType 业务类型
         * @return 规则引擎建造者
         */
        public Builder<T> businessType(String businessType) {
            abstractRulesEngine.setBusinessType(businessType);
            return this;
        }

        /**
         * 设置规则工厂
         *
         * @param ruleFactory 规则工厂
         * @return 规则引擎建造者
         */
        public Builder<T> ruleFactory(RuleFactory ruleFactory) {
            abstractRulesEngine.setRuleFactory(ruleFactory);
            return this;
        }

        /**
         * 设置bean解析器
         *
         * @param beanResolver bean解析器
         * @return 规则引擎建造者
         */
        public Builder<T> beanResolver(BeanResolver beanResolver) {
            abstractRulesEngine.setBeanResolver(beanResolver);
            return this;
        }

        /**
         * 设置全局函数
         *
         * @param globalFunctions 全局函数方法
         * @return 规则引擎建造者
         */
        public Builder<T> globalFunctions(List<Method> globalFunctions) {
            abstractRulesEngine.setGlobalFunctions(globalFunctions);
            return this;
        }

        /**
         * 实例化规则引擎
         *
         * @return 规则引擎实例
         */
        public T build() {
            if (abstractRulesEngine.getBusinessType() == null) {
                throw new IllegalArgumentException("there is not a businessType to be set in builder.");
            }
            if (abstractRulesEngine.getRuleFactory() == null) {
                throw new IllegalArgumentException("there is not a ruleFactory to be set in builder.");
            }
            this.abstractRulesEngine.reloadRules();
            return this.abstractRulesEngine;
        }
    }
}
