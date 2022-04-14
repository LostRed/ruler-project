package info.lostred.ruler.factory;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.GlobalConfiguration;
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
     * 获取规则
     *
     * @param ruleCode 规则编码
     * @param <E>      规则约束的参数类型
     * @return 规则
     */
    <E> AbstractRule<E> getRule(String ruleCode);

    /**
     * 获取ruler全局配置
     *
     * @return ruler全局配置
     */
    GlobalConfiguration getGlobalConfiguration();

    /**
     * 获取规则集合
     *
     * @param businessType 业务类型
     * @param validClass   规则约束类的类对象
     * @param <E>          规则约束的参数类型
     * @return 规则集合
     */
    <E> List<AbstractRule<E>> getRules(String businessType, Class<E> validClass);
}
