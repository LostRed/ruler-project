package com.ylzinfo.ruler.support;

import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 规则工厂
 *
 * @author dengluwei
 */
public final class RuleFactory {

    private RuleFactory() {
    }

    /**
     * 获取单规则的建造者
     *
     * @param config     规则配置
     * @param ruleInfo   规则信息
     * @param validClass 规则约束类的类对象
     * @param <E>        规则约束的参数类型
     * @return 某个规则的建造者实例对象
     */
    public static <E> RuleBuilder<E> ruleBuilder(ValidConfiguration config, RuleInfo ruleInfo, Class<E> validClass) {
        return new RuleBuilder<>(config, ruleInfo);
    }

    /**
     * 获取规则集合的建造者
     *
     * @param config     规则配置
     * @param ruleInfos  规则信息集合
     * @param validClass 规则约束类的类对象
     * @param <E>        规则约束的参数类型
     * @return 某个规则集合的建造者实例对象
     */
    public static <E> RulesBuilder<E> rulesBuilder(ValidConfiguration config, Collection<RuleInfo> ruleInfos, Class<E> validClass) {
        return new RulesBuilder<>(config, ruleInfos);
    }

    /**
     * 实例化规则
     *
     * @param ruleInfo 规则信息
     * @param <E>      规则约束的参数类型
     * @return 规则实例对象
     */
    @SuppressWarnings("unchecked")
    public static <E> Rule<E> buildRule(ValidConfiguration config, RuleInfo ruleInfo) {
        try {
            Class<?> ruleClass = Class.forName(ruleInfo.getRuleClassName());
            Constructor<?> constructor = ruleClass.getDeclaredConstructor(ValidConfiguration.class, RuleInfo.class);
            Object object = constructor.newInstance(config, ruleInfo);
            if (object instanceof Rule<?>) {
                return (Rule<E>) object;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Internal error: " + ruleInfo.getRuleClassName() + " cannot be instantiated.", e);
        }
        throw new RuntimeException("Internal error: " + ruleInfo.getRuleClassName() + " cannot be instantiated, because it is not instance of Rule.");
    }

    /**
     * 规则建造者
     */
    public static class RuleBuilder<E> {
        private final Rule<E> rule;

        public RuleBuilder(ValidConfiguration config, RuleInfo ruleInfo) {
            this.rule = buildRule(config, ruleInfo);
        }

        public Rule<E> build() {
            return this.rule;
        }
    }

    /**
     * 规则集合建造者
     */
    public static class RulesBuilder<E> {
        private final List<Rule<E>> rules = new ArrayList<>();

        public RulesBuilder(ValidConfiguration config, Collection<RuleInfo> ruleInfos) {
            for (RuleInfo ruleInfo : ruleInfos) {
                rules.add(buildRule(config, ruleInfo));
            }
        }

        public List<Rule<E>> build() {
            return this.rules;
        }
    }
}
