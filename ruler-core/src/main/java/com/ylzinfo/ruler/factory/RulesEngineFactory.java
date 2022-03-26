package com.ylzinfo.ruler.factory;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.support.TypeReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 规则引擎工厂
 *
 * @author dengluwei
 */
public final class RulesEngineFactory {

    private RulesEngineFactory() {
    }

    /**
     * 获取工厂的建造者对象
     *
     * @param ruleFactory     规则管理器
     * @param businessType    业务类型
     * @param rulesEngineType 规则引擎类型
     * @param validClass      规则约束的参数类型
     * @param <T>             规则引擎类型
     * @param <E>             规则约束的参数类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    public static <T extends RulesEngine<E>, E> Builder<T, E> builder(RuleFactory ruleFactory, String businessType, Class<T> rulesEngineType, Class<E> validClass) {
        return new Builder<>(ruleFactory, businessType, rulesEngineType, validClass);
    }

    /**
     * 获取工厂的建造者对象
     *
     * @param ruleFactory   规则管理器
     * @param businessType  业务类型
     * @param typeReference 类型引用
     * @param <T>           规则引擎类型
     * @param <E>           规则约束的参数类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    public static <T extends RulesEngine<E>, E> Builder<T, E> builder(RuleFactory ruleFactory, String businessType, TypeReference<T> typeReference) {
        return new Builder<>(ruleFactory, businessType, typeReference);
    }

    /**
     * 规则引擎建造者
     *
     * @param <T> 规则引擎类型
     * @param <E> 规则约束的参数类型
     */
    public static class Builder<T extends RulesEngine<E>, E> {
        private final RuleFactory ruleFactory;
        private final String businessType;
        private final Class<E> validClass;
        private final Class<T> rulesEngineType;

        public Builder(RuleFactory ruleFactory, String businessType, Class<T> rulesEngineType, Class<E> validClass) {
            this.rulesEngineType = rulesEngineType;
            this.validClass = validClass;
            this.ruleFactory = ruleFactory;
            this.businessType = businessType;
        }

        @SuppressWarnings("unchecked")
        public Builder(RuleFactory ruleFactory, String businessType, TypeReference<T> typeReference) {
            Type type = typeReference.getType();
            if (type instanceof ParameterizedType) {
                this.rulesEngineType = (Class<T>) ((ParameterizedType) type).getRawType();
                this.validClass = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
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
        public T build() {
            try {
                Constructor<T> constructor = rulesEngineType.getDeclaredConstructor(String.class, Collection.class);
                List<AbstractRule<E>> rules = ruleFactory.getRules(businessType, validClass);
                if (rules.isEmpty()) {
                    throw new IllegalArgumentException("Cannot get available rules.");
                }
                return constructor.newInstance(businessType, rules);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            throw new IllegalArgumentException("Fail to build Engine, because the type is illegal.");
        }
    }
}
