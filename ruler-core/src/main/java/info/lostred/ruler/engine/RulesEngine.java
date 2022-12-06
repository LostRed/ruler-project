package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.EvaluationContext;

import java.util.Collection;
import java.util.List;

/**
 * 规则引擎接口
 *
 * @author lostred
 */
public interface RulesEngine {
    /**
     * 创建评估上下文
     *
     * @param rootObject 根对象
     * @return 评估上下文
     */
    EvaluationContext createEvaluationContext(Object rootObject);

    /**
     * 执行
     *
     * @param context 评估上下文
     */
    void execute(EvaluationContext context);

    /**
     * 获取执行结果
     *
     * @param context 评估上下文
     * @return 执行结果
     */
    Result getResult(EvaluationContext context);

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
