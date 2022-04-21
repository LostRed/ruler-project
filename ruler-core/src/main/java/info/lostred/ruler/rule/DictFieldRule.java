package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constants.RulerConstants;
import info.lostred.ruler.constants.ValidType;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典字段校验规则
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
@Rule(ruleCode = "dict", businessType = RulerConstants.COMMON_BUSINESS_TYPE, desc = "规定的字段必须填写字典中的值")
public class DictFieldRule<T> extends SingleFieldRule<T> {

    public DictFieldRule(RuleInfo ruleInfo, ValidConfiguration validConfiguration) {
        super(ruleInfo, validConfiguration);
    }

    @Override
    public boolean isSupported(T object) {
        return validConfiguration != null
                && !validConfiguration.getValidInfos(ValidType.DICT.name()).isEmpty();
    }

    @Override
    public boolean judge(T object) {
        return validConfiguration.getValidInfos(ValidType.DICT.name()).stream()
                .anyMatch(validInfo -> this.check(object, validInfo));
    }

    @Override
    public Report buildReport(T object) {
        Map<String, Object> map = validConfiguration.getValidInfos(ValidType.DICT.name()).stream()
                .flatMap(validInfo -> this.collectEntries(object, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegals(map);
    }

    @Override
    protected boolean isIllegal(ValidInfo validInfo, Object value) {
        Set<Object> dict = validInfo.getDict();
        if (dict != null && value != null && !"".equals(value)) {
            return !dict.contains(value);
        }
        return false;
    }
}
