package info.lostred.ruler.core2;

import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.Reportable;
import info.lostred.ruler.domain.RuleInfo;

/**
 * 抽象规则
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class AbstractRule<E> implements Judgement<E>, Reportable<E> {
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
