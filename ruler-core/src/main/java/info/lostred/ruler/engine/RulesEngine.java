package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;

/**
 * 规则引擎接口
 *
 * @author lostred
 */
public interface RulesEngine {
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
     * 添加规则并按顺序号排序
     *
     * @param rule 规则
     */
    void addRule(AbstractRule rule);

    /**
     * 添加规则并按顺序号排序
     *
     * @param ruleCode 规则编号
     */
    void addRule(String ruleCode);

    /**
     * 添加规则并按顺序号排序
     *
     * @param ruleCodes 规则编号集合
     */
    void addRule(Collection<String> ruleCodes);

    /**
     * 移除规则
     *
     * @param ruleCode 规则编号
     * @return 成功移除返回true，否则返回false
     */
    boolean removeRule(String ruleCode);

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