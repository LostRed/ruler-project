package com.ylzinfo.ruler.support;

import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.RulesEngine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
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
     * @param typeReference 类型引用
     * @param rules         规则集合
     * @param <T>           规则引擎类型
     * @param <E>           规则约束参数类型
     * @return 某个规则引擎类型的建造者实例对象
     */
    public static <T extends RulesEngine<E>, E> Builder<T, E> builder(TypeReference<T> typeReference, List<Rule<E>> rules) {
        return new Builder<>(typeReference, rules);
    }

    /**
     * 规则引擎建造者
     *
     * @param <T> 规则引擎类型
     * @param <E> 规则约束的参数类型
     */
    public static class Builder<T extends RulesEngine<E>, E> {
        /**
         * 暂存的泛型类型
         */
        private Type type;
        /**
         * 暂存的规则集合
         */
        private final List<Rule<E>> rules;

        public Builder(TypeReference<T> typeReference, List<Rule<E>> rules) {
            Type type = typeReference.getType();
            if (type instanceof ParameterizedType) {
                this.type = ((ParameterizedType) type).getRawType();
            }
            this.rules = rules;
            this.rules.sort(Comparator.comparing(e -> e.getRuleInfo().getSeq()));
        }

        /**
         * 实例化规则引擎
         *
         * @return 规则引擎实例
         */
        @SuppressWarnings("unchecked")
        public T build() {
            if (type instanceof Class) {
                try {
                    Constructor<T> constructor = ((Class<T>) type).getDeclaredConstructor(Collection.class);
                    return constructor.newInstance(rules);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException(e);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            throw new IllegalArgumentException("Engine build fail, the type is illegal.");
        }
    }
}
