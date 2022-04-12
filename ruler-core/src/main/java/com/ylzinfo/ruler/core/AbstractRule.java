package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.RuleInfo;

/**
 * 抽象规则
 *
 * @param <T> 规则约束的参数类型
 * @author dengluwei
 */
public abstract class AbstractRule<T> implements Judgement<T>, Reporter<T> {
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
