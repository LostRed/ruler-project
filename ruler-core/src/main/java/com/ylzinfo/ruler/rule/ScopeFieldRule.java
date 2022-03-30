package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;

public abstract class ScopeFieldRule<E> extends SingleFieldRule<E> {

    public ScopeFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    /**
     * 违规值后追加参考值
     *
     * @param value 违规值
     * @param lower 上限值
     * @param upper 下限值
     * @return 带参考值的违规值
     */
    protected Object appendReference(Object value, Object lower, Object upper) {
        lower = lower == null ? "-∞" : lower;
        upper = upper == null ? "+∞" : upper;
        return value + " (" + lower + " ~ " + upper + ")";
    }
}
