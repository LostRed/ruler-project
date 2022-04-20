package info.lostred.ruler.factory;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.domain.RuleInfo;

import java.util.List;

/**
 * 规则工厂
 *
 * @author lostred
 */
public interface RuleFactory {
    /**
     * 初始化工厂
     */
    void init();

    /**
     * 注册规则信息
     *
     * @param ruleInfo 规则信息
     */
    void registerRuleInfo(RuleInfo ruleInfo);

    /**
     * 创建规则
     *
     * @param ruleInfo 规则信息
     */
    void createRule(RuleInfo ruleInfo);

    /**
     * 根据规则编号获取规则
     *
     * @param ruleCode 规则编号
     * @param <E>      规则约束的参数类型
     * @return 规则
     */
    <E> AbstractRule<E> getRule(String ruleCode);

    /**
     * 根据业务类型与约束对象类型查找规则集合
     *
     * @param businessType 业务类型
     * @param <E>          规则约束的参数类型
     * @return 规则集合
     */
    <E> List<AbstractRule<E>> findRules(String businessType);
}
