package info.lostred.ruler.factory;

import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.RulesEngine;
import info.lostred.ruler.support.TypeReference;

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
public interface RulesEngineFactory {
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
    static  <T extends RulesEngine<E>, E> Builder<T, E> builder(RuleFactory ruleFactory, String businessType, Class<T> rulesEngineType, Class<E> validClass) {
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
    static <T extends RulesEngine<E>, E> Builder<T, E> builder(RuleFactory ruleFactory, String businessType, TypeReference<T> typeReference) {
        return new Builder<>(ruleFactory, businessType, typeReference);
    }

    /**
     * 分配规则引擎
     *
     * @param validRootNode 校验根节点
     * @param validClass    规则约束类的类对象
     * @param <E>           规则约束的参数类型
     * @return 规则引擎
     */
    <E> RulesEngine<E> dispatch(Object validRootNode, Class<E> validClass);

    /**
     * 分配规则引擎
     *
     * @param businessType  业务类型
     * @param validRootNode 校验根节点
     * @param validClass    规则约束类的类对象
     * @param <E>           规则约束的参数类型
     * @return 规则引擎
     */
    <E> RulesEngine<E> dispatch(String businessType, Object validRootNode, Class<E> validClass);

    /**
     * 规则引擎建造者
     *
     * @param <T> 规则引擎类型
     * @param <E> 规则约束的参数类型
     */
    class Builder<T extends RulesEngine<E>, E> {
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
                Constructor<T> constructor = rulesEngineType.getDeclaredConstructor(RuleFactory.class, String.class, Collection.class);
                List<AbstractRule<E>> rules = ruleFactory.getRules(RulerConstants.DEFAULT_BUSINESS_TYPE, validClass);
                if (!RulerConstants.DEFAULT_BUSINESS_TYPE.equals(businessType)) {
                    rules.addAll(ruleFactory.getRules(businessType, validClass));
                }
                if (rules.isEmpty()) {
                    throw new IllegalArgumentException("This engine's business type is '" + businessType + "', has not available rules.");
                }
                return constructor.newInstance(ruleFactory, businessType, rules);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
