package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.constants.ValidType;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 必填字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
@Rule(ruleCode = "required", desc = "规定的字段必须填写")
public class RequiredFieldRule<E> extends SingleFieldRule<E> {

    public RequiredFieldRule(RuleInfo ruleInfo, ValidConfiguration validConfiguration) {
        super(ruleInfo, validConfiguration);
    }

    @Override
    public boolean isSupported(E element) {
        return !validConfiguration.getValidInfos(ValidType.REQUIRED.name()).isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return validConfiguration.getValidInfos(ValidType.REQUIRED.name()).stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = validConfiguration.getValidInfos(ValidType.REQUIRED.name()).stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegals(map);
    }

    @Override
    protected boolean isIllegal(ValidInfo validInfo, Object value) {
        return value == null || "".equals(value);
    }

    @Override
    protected Set<Map.Entry<String, Object>> wrapToSet(E element, ValidInfo validInfo, Object value) {
        return super.wrapToSet(element, validInfo, "-");
    }
}
