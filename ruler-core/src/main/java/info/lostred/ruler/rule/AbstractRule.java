package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import info.lostred.ruler.domain.RuleDefinition;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Evaluator {
    /**
     * 规则定义
     */
    private RuleDefinition ruleDefinition;

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public void setRuleDefinition(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    /**
     * 规则初始化
     */
    public void init() {
    }

    /**
     * 规则是否支持对该参数进行判断
     *
     * @param object 参数
     * @return 支持返回true，否则返回false
     */
    public abstract boolean supports(Object object);

    /**
     * 获取需要记录的值
     *
     * @param object 参数
     * @return 需要记录的值
     */
    public abstract Object getValue(Object object);
}
