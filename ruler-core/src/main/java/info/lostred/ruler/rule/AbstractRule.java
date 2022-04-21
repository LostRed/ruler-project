package info.lostred.ruler.rule;

import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.Reportable;
import info.lostred.ruler.domain.RuleInfo;

/**
 * 抽象规则
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
public abstract class AbstractRule<T> implements Judgement<T>, Reportable<T> {
    /**
     * 规则信息
     */
    protected final RuleInfo ruleInfo;

    public AbstractRule(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }
}
