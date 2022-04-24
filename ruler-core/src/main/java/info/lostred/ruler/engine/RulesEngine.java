package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;
import java.util.List;

/**
 * 规则引擎接口
 *
 * @author lostred
 */
public interface RulesEngine {
    /**
     * 获取引擎的业务类型
     *
     * @return 业务类型
     */
    String getBusinessType();

    /**
     * 执行规则
     *
     * @param object 待校验的对象
     * @return 引擎执行的结果
     */
    Result execute(Object object);

    /**
     * 评估结果
     *
     * @param object 待校验的对象
     * @return 引擎执行的布尔结果，true为不通过，false为通过
     */
    boolean evaluate(Object object);

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
     * 重新载入规则
     */
    void reloadRules();

    /**
     * 设置bean解析器
     *
     * @param context 评估上下文
     */
    void setBeanResolver(StandardEvaluationContext context);

    /**
     * 设置评估上下文参数
     *
     * @param context 评估上下文
     * @param name    参数名
     * @param object  参数
     */
    void setVariable(StandardEvaluationContext context, String name, Object object);
}
