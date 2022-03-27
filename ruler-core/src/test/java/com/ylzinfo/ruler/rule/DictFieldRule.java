package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
@Rule(ruleCode = "common_2", businessType = "common", desc = "规定的字段必须填写字典中的值")
public class DictFieldRule<E> extends SingleFieldRule<E> {

    public DictFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !validConfiguration.getDict().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return validConfiguration.getDictValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = validConfiguration.getDictValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this.getReport(this.ruleInfo, element, map);
    }

    @Override
    protected boolean isNotMatch(ValidInfo validInfo, Object value) {
        Set<Object> set = validConfiguration.getDict().get(validInfo.getFieldName());
        if (set != null && value != null && !"".equals(value)) {
            return !set.contains(value);
        }
        return false;
    }
}
