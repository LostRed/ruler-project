package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.GlobalConfiguration;
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
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
@Rule(ruleCode = "number_scope", desc = "规定的数值字段必须在限定的范围内")
public class NumberScopeFieldRule<E> extends ScopeFieldRule<E> {

    public NumberScopeFieldRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !globalConfiguration.getNumberScopeValidInfos().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return globalConfiguration.getNumberScopeValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = globalConfiguration.getNumberScopeValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
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
    protected Set<Map.Entry<String, Object>> wrap(E element, ValidInfo validInfo, Object value) {
        BigDecimal lowerLimit = validInfo.getLowerLimit();
        BigDecimal upperLimit = validInfo.getUpperLimit();
        return super.wrap(element, validInfo, this.appendReference(value, lowerLimit, upperLimit));
    }
}
