package com.ylzinfo.ruler.core;

/**
 * 规则引擎工厂
 *
 * @author dengluwei
 */
public interface RulesEngineFactory {
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
}
