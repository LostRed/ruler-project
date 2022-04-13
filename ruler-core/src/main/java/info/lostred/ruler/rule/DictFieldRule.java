package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
@Rule(ruleCode = "dict", desc = "规定的字段必须填写字典中的值")
public class DictFieldRule<E> extends SingleFieldRule<E> {

    public DictFieldRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !globalConfiguration.getDictValidInfos().isEmpty()
                && !globalConfiguration.getDict().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return globalConfiguration.getDictValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = globalConfiguration.getDictValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegals(map);
    }

    @Override
    protected boolean isIllegal(ValidInfo validInfo, Object value) {
        Set<Object> set = globalConfiguration.getDict().get(validInfo);
        if (set != null && value != null && !"".equals(value)) {
            return !set.contains(value);
        }
        return false;
    }
}
