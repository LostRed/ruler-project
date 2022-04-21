package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.constants.ValidType;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数值范围字段校验规则
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
@Rule(ruleCode = "number_scope", businessType = RulerConstants.DEFAULT_BUSINESS_TYPE, desc = "规定的数值字段必须在限定的范围内")
public class NumberScopeFieldRule<T> extends ScopeFieldRule<T> {

    public NumberScopeFieldRule(RuleInfo ruleInfo, ValidConfiguration validConfiguration) {
        super(ruleInfo, validConfiguration);
    }

    @Override
    public boolean isSupported(T object) {
        return validConfiguration != null
                && !validConfiguration.getValidInfos(ValidType.NUMBER_SCOPE.name()).isEmpty();
    }

    @Override
    public boolean judge(T object) {
        return validConfiguration.getValidInfos(ValidType.NUMBER_SCOPE.name()).stream()
                .anyMatch(validInfo -> this.check(object, validInfo));
    }

    @Override
    public Report buildReport(T object) {
        Map<String, Object> map = validConfiguration.getValidInfos(ValidType.NUMBER_SCOPE.name()).stream()
                .flatMap(validInfo -> this.collectEntries(object, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegals(map);
    }

    @Override
    protected boolean isIllegal(ValidInfo validInfo, Object value) {
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
    protected Set<Map.Entry<String, Object>> collect(ValidInfo validInfo, String nodeTrace, Object value) {
        BigDecimal lowerLimit = validInfo.getLowerLimit();
        BigDecimal upperLimit = validInfo.getUpperLimit();
        return super.collect(validInfo, nodeTrace, this.appendReference(value, lowerLimit, upperLimit));
    }
}
