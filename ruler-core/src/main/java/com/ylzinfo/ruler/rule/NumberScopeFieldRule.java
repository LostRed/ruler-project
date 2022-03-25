package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数值范围字段校验规则
 *
 * @param <T> 规则约束的参数类型
 * @author dengluwei
 */
public class NumberScopeFieldRule<T> extends SingleFieldValidRule<T> {

    public NumberScopeFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(T element) {
        return !validConfiguration.getScopeValidInfos().isEmpty();
    }

    @Override
    public boolean judge(T element) {
        return validConfiguration.getScopeValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(T element) {
        Map<String, Object> map = validConfiguration.getScopeValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this.getReport(this.ruleInfo, map);
    }

    @Override
    protected boolean match(ValidInfo validInfo, Object value) {
        if (value != null && !"".equals(value)) {
            if (value instanceof Number) {
                BigDecimal bigDecimal = new BigDecimal(value.toString());
                BigDecimal lowerLimit = validInfo.getLowerLimit();
                BigDecimal upperLimit = validInfo.getUpperLimit();
                if (lowerLimit != null) {
                    return bigDecimal.compareTo(lowerLimit) < 0;
                }
                if (upperLimit != null) {
                    return bigDecimal.compareTo(upperLimit) > 0;
                }
            }
        }
        return false;
    }

    @Override
    protected Set<Map.Entry<String, Object>> wrap(ValidInfo validInfo, Object value) {
        BigDecimal lowerLimit = validInfo.getLowerLimit();
        BigDecimal upperLimit = validInfo.getUpperLimit();
        String lower = lowerLimit == null ? "-∞" : lowerLimit.toString();
        String upper = upperLimit == null ? "+∞" : upperLimit.toString();
        value = value + " (参考值: " + lower + " ~ " + upper + ")";
        return super.wrap(validInfo, value);
    }
}
