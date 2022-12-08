package info.lostred.ruler.factory;

import info.lostred.ruler.constant.RulerConstants;
import info.lostred.ruler.engine.AbstractRulesEngine;
import info.lostred.ruler.engine.RulesEngine;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

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
        private final Class<T> rulesEngineClass;
        private String businessType = RulerConstants.BUSINESS_TYPE_COMMON;
        private RuleFactory ruleFactory;
        private ExpressionParser expressionParser;
        private BeanResolver beanResolver;
        private List<Method> globalFunctions;

        public Builder(Class<T> rulesEngineClass) {
            this.rulesEngineClass = rulesEngineClass;
        }

        /**
         * 设置业务类型
         *
         * @param businessType 业务类型
         * @return 规则引擎建造者
         */
        public Builder<T> setBusinessType(String businessType) {
            this.businessType = businessType;
            return this;
        }

        /**
         * 设置规则工厂
         *
         * @param ruleFactory 规则工厂
         * @return 规则引擎建造者
         */
        public Builder<T> setRuleFactory(RuleFactory ruleFactory) {
            this.ruleFactory = ruleFactory;
            return this;
        }

        /**
         * 设置spEL表达式解析器
         *
         * @param expressionParser 表达式解析器
         * @return 规则引擎建造者
         */
        public Builder<T> setExpressionParser(ExpressionParser expressionParser) {
            this.expressionParser = expressionParser;
            return this;
        }

        /**
         * 设置bean解析器
         *
         * @param beanResolver bean解析器
         * @return 规则引擎建造者
         */
        public Builder<T> setBeanResolver(BeanResolver beanResolver) {
            this.beanResolver = beanResolver;
            return this;
        }

        /**
         * 设置全局函数
         *
         * @param globalFunctions 全局函数方法
         * @return 规则引擎建造者
         */
        public Builder<T> setGlobalFunctions(List<Method> globalFunctions) {
            this.globalFunctions = globalFunctions;
            return this;
        }

        /**
         * 实例化规则引擎
         *
         * @return 规则引擎实例
         */
        public T build() {
            if (ruleFactory == null) {
                throw new IllegalArgumentException("there is not a ruleFactory to be set in builder.");
            }
            if (expressionParser == null) {
                throw new IllegalArgumentException("there is not an expressionParser to be set in builder.");
            }
            try {
                Constructor<T> constructor = rulesEngineClass.getDeclaredConstructor(
                        String.class, RuleFactory.class, ExpressionParser.class, BeanResolver.class, List.class);
                return constructor.newInstance(businessType, ruleFactory, expressionParser, beanResolver, globalFunctions);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
