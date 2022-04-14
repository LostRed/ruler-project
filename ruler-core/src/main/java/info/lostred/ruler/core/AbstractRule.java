package info.lostred.ruler.core;

import info.lostred.ruler.domain.RuleInfo;

/**
 * 抽象规则
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class AbstractRule<E> implements Judgement<E>, Reportable<E> {
    /**
     * 规则配置
     */
    protected final GlobalConfiguration globalConfiguration;
    /**
     * 规则信息
     */
    protected final RuleInfo ruleInfo;

    public AbstractRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        this.globalConfiguration = globalConfiguration;
        this.ruleInfo = ruleInfo;
    }

    public GlobalConfiguration getValidConfiguration() {
        return globalConfiguration;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }
}
