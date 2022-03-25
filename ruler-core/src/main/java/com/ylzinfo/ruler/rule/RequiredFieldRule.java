package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 必填字段校验规则
 *
 * @param <T> 规则约束的参数类型
 * @author dengluwei
 */
public class RequiredFieldRule<T> extends SingleFieldValidRule<T> {

    public RequiredFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(T element) {
        return !validConfiguration.getRequiredValidInfos().isEmpty();
    }

    @Override
    public boolean judge(T element) {
        return validConfiguration.getRequiredValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(T element) {
        Map<String, Object> map = validConfiguration.getRequiredValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this.getReport(this.ruleInfo, map);
    }

    @Override
    protected boolean match(ValidInfo validInfo, Object value) {
        return value == null || "".equals(value);
    }

    @Override
    protected Set<Map.Entry<String, Object>> wrap(ValidInfo validInfo, Object value) {
        if (value == null) {
            return super.wrap(validInfo, "-");
        }
        return super.wrap(validInfo, value);
    }
}
