package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;

import java.util.List;
import java.util.Map;

/**
 * 规则工厂
 *
 * @author lostred
 */
public interface RuleFactory {
    /**
     * 注册规则定义
     *
     * @param ruleDefinition 规则定义
     * @throws RulesException 注册规则定义失败时
     */
    void registerRuleDefinition(RuleDefinition ruleDefinition) throws RulesException;

    /**
     * 获取规则定义
     *
     * @return 不可修改的规则定义集合
     */
    Map<String, RuleDefinition> getRuleDefinitions();

    /**
     * 根据规则编号获取规则
     *
     * @param ruleCode 规则编号
     * @return 规则
     */
    AbstractRule getRule(String ruleCode);

    /**
     * 根据规则类型获取规则
     *
     * @param ruleClass 规则类型
     * @return 规则
     */
    AbstractRule getRule(Class<? extends AbstractRule> ruleClass);

    /**
     * 根据业务类型与约束对象类型查找规则集合
     *
     * @param businessType 业务类型
     * @return 规则集合
     */
    List<AbstractRule> getRulesWithBusinessType(String businessType);

    /**
     * 注销规则
     *
     * @param ruleCode 规则编号
     * @return 被注销的规则
     */
    AbstractRule unregisterRule(String ruleCode);

    /**
     * 注销所有规则
     */
    void unregisterAllRules();
}
