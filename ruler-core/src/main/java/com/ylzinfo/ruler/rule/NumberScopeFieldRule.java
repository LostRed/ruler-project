package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.annotation.Rule;
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
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
@Rule(ruleCode = "number_scope", desc = "规定的数值字段必须在限定的范围内")
public class NumberScopeFieldRule<E> extends ScopeFieldRule<E> {

    public NumberScopeFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !validConfiguration.getNumberScopeValidInfos().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return validConfiguration.getNumberScopeValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = validConfiguration.getNumberScopeValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegal(map);
    }

    @Override
    protected boolean isNotMatch(ValidInfo validInfo, Object value) {
        if (value instanceof Number) {
            BigDecimal lowerLimit = validInfo.getLowerLimit();
            BigDecimal upperLimit = validInfo.getUpperLimit();
            BigDecimal bigDecimal = new BigDecimal(value.toString());
            boolean lower = false;
            boolean upper = false;
            if (lowerLimit != null) {
                lower = bigDecimal.compareTo(lowerLimit) < 0;
            }
            if (upperLimit != null) {
                upper = bigDecimal.compareTo(upperLimit) > 0;
            }
            return lower || upper;
        }
        return false;
    }

    @Override
    protected Set<Map.Entry<String, Object>> wrap(E element, ValidInfo validInfo, Object value) {
        BigDecimal lowerLimit = validInfo.getLowerLimit();
        BigDecimal upperLimit = validInfo.getUpperLimit();
        return super.wrap(element, validInfo, this.appendReference(value, lowerLimit, upperLimit));
    }
}
