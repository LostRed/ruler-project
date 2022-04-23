package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.ExpressionParser;

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
     */
    void registerRuleDefinition(RuleDefinition ruleDefinition);

    /**
     * 注册规则
     *
     * @param rule 规则
     */
    void registerRule(AbstractRule rule);

    /**
     * 创建规则
     *
     * @param ruleDefinition 规则定义
     * @param parser         表达式解析器
     */
    void createRule(RuleDefinition ruleDefinition, ExpressionParser parser);

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
