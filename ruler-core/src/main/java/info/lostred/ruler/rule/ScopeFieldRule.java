package info.lostred.ruler.rule;

import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.RuleInfo;

/**
 * 范围字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class ScopeFieldRule<E> extends SingleFieldRule<E> {

    public ScopeFieldRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
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
        return value + " (参考值: " + lower + " ~ " + upper + ")";
    }
}
