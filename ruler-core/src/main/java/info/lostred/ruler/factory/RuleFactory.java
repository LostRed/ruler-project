package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.exception.RulesException;
import info.lostred.ruler.rule.AbstractRule;

import java.util.List;

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
     * 注册规则及其规则定义
     *
     * @param rule 规则
     */
    void registerRule(AbstractRule rule);

    /**
     * 创建规则
     *
     * @param ruleDefinition 规则定义
     * @return 抽象规则
     * @throws RulesException 实例化规则失败时
     */
    AbstractRule createRule(RuleDefinition ruleDefinition) throws RulesException;

    /**
     * 销毁所有规则
     */
    void destroyAllRules();

    /**
     * 销毁规则
     *
     * @param ruleCode 规则编号
     * @return 被销毁的规则
     */
    AbstractRule destroyRule(String ruleCode);

    /**
     * 根据规则编号获取规则
     *
     * @param ruleCode 规则编号
     * @return 规则
     */
    AbstractRule getRule(String ruleCode);

    /**
     * 根据业务类型与约束对象类型查找规则集合
     *
     * @param businessType 业务类型
     * @return 规则集合
     */
    List<AbstractRule> findRules(String businessType);

    /**
     * 获取所有规则定义
     *
     * @return 规则定义集合
     */
    List<RuleDefinition> getRuleDefinitions();
}
