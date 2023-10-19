package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 规则引擎接口
 *
 * @author lostred
 */
public interface RulesEngine {
    /**
     * 执行
     *
     * @param rootObject 根对象
     * @return 执行结果
     */
    Result execute(Object rootObject);

    /**
     * 使用特定规则执行
     *
     * @param rootObject 根对象
     * @param ruleCodes  规则编号
     * @return 执行结果
     */
    Result executeWithRules(Object rootObject, Set<String> ruleCodes);

    /**
     * 获取引擎的业务类型
     *
     * @return 业务类型
     */
    String getBusinessType();

    /**
     * 获取引擎中的所有规则定义
     *
     * @return 规则定义集合
     */
    List<RuleDefinition> getRuleDefinitions();

    /**
     * 根据规则编号获取引擎中的规则
     *
     * @param ruleCode 规则编号
     * @return 规则
     */
    AbstractRule getRule(String ruleCode);

    /**
     * 根据规则类型获取引擎中的规则
     *
     * @param ruleType 规则类型
     * @return 规则
     */
    List<AbstractRule> getRules(String ruleType);

    /**
     * 在引擎中添加规则并按顺序号排序
     *
     * @param ruleCode 规则编号
     */
    void addRule(String ruleCode);

    /**
     * 在引擎中添加规则并按顺序号排序
     *
     * @param ruleCodes 规则编号集合
     */
    void addRule(Collection<String> ruleCodes);

    /**
     * 移除规则
     *
     * @param ruleCode 规则编号
     * @return 移除的规则
     */
    AbstractRule removeRule(String ruleCode);

    /**
     * 强制移除规则
     * <p>用于规则工厂删除规则后</p>
     *
     * @param ruleCode 规则编号
     */
    void forceRemoveRule(String ruleCode);

    /**
     * 重新载入规则
     * <p>重新初始化规则引擎中的规则</p>
     */
    void reloadRules();
}
