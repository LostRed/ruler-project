package info.lostred.ruler.factory;

import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.support.TypeReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 规则引擎工厂
 *
 * @author lostred
 */
public interface RulesEngineFactory {
    /**
     * 获取工厂的建造者对象
     *
     * @param ruleFactory     规则管理器
     * @param businessType    业务类型
     * @param rulesEngineType 规则引擎类型
     * @param validClass      规则约束类的类对象
     * @param <U>             规则引擎类型
     * @param <T>             规则约束的参数类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    static <U extends RulesEngine, T> Builder<U, T> builder(RuleFactory ruleFactory, String businessType, Class<U> rulesEngineType, Class<T> validClass) {
        return new Builder<>(ruleFactory, businessType, rulesEngineType, validClass);
    }

    /**
     * 获取工厂的建造者对象
     *
     * @param ruleFactory   规则管理器
     * @param businessType  业务类型
     * @param typeReference 类型引用
     * @param <U>           规则引擎类型
     * @param <T>           规则约束的参数类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    static <U extends RulesEngine, T> Builder<U, T> builder(RuleFactory ruleFactory, String businessType, TypeReference<U> typeReference) {
        return new Builder<>(ruleFactory, businessType, typeReference);
    }

    /**
     * 从引擎单例池中获取规则引擎
     *
     * @param businessType 业务类型
     * @return 规则引擎
     */
    RulesEngine getEngine(String businessType);

    /**
     * 规则引擎建造者
     *
     * @param <U> 规则引擎类型
     * @param <T> 规则约束的参数类型
     */
    class Builder<U extends RulesEngine, T> {
        private final RuleFactory ruleFactory;
        private final String businessType;
        private final Class<U> rulesEngineType;

        private Builder(RuleFactory ruleFactory, String businessType, Class<U> rulesEngineType, Class<T> validClass) {
            this.rulesEngineType = rulesEngineType;
            this.ruleFactory = ruleFactory;
            this.businessType = businessType;
        }

        @SuppressWarnings("unchecked")
        private Builder(RuleFactory ruleFactory, String businessType, TypeReference<U> typeReference) {
            Type type = typeReference.getType();
            if (type instanceof ParameterizedType) {
                this.rulesEngineType = (Class<U>) ((ParameterizedType) type).getRawType();
            } else {
                throw new IllegalArgumentException("The typeReference's type is not a parameterized type.");
            }
            this.ruleFactory = ruleFactory;
            this.businessType = businessType;
        }

        /**
         * 实例化规则引擎
         *
         * @return 规则引擎实例
         */
        public U build() {
            try {
                Constructor<U> constructor = rulesEngineType.getDeclaredConstructor(RuleFactory.class, String.class);
                return constructor.newInstance(ruleFactory, businessType);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
