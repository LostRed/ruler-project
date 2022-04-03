package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.util.ReflectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验报告生成接口
 *
 * @param <E> 报告数据来源的参数类型
 * @author dengluwei
 */
public interface Reporter<E> {
    /**
     * 生成校验报告
     *
     * @param element 参数
     * @return 报告
     */
    Report buildReport(E element);

    /**
     * 将非法字段与值包装成报告
     *
     * @param element   规则约束的对象
     * @param fieldName 字段名
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    default Report getReport(RuleInfo ruleInfo, E element, String fieldName, Object value) {
        Map<String, Object> map = new HashMap<>();
        try {
            String fieldTrace = ReflectUtils.getFieldTrace(element.getClass(), element.getClass(),
                    fieldName, value);
            map.put(fieldTrace, value);
        } catch (NoSuchFieldException e) {
            map.put(fieldName, value);
        }
        return Report.of(ruleInfo).putIllegals(map);
    }
}
