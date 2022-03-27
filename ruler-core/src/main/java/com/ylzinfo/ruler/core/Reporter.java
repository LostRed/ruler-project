package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param validNode 校验节点
     * @param fieldName 字段名
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    default Report getReport(RuleInfo ruleInfo, Object validNode, String fieldName, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(this.fieldKey(validNode.getClass(), fieldName), value);
        return Report.of(ruleInfo).putIllegal(map);
    }

    /**
     * 将非法字段与值的集合包装成报告
     *
     * @param validNode 校验节点
     * @param illegals  非法字段与值的集合
     * @return 非法字段与值的集合
     */
    default Report getReport(RuleInfo ruleInfo, Object validNode, Map<String, Object> illegals) {
        Class<?> validClass = validNode.getClass();
        Map<String, Object> map = illegals.entrySet().stream()
                .collect(Collectors.toMap(e -> this.fieldKey(validClass, e.getKey()), Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegal(map);
    }

    /**
     * 获取校验字段的key
     *
     * @param validInfo 校验信息
     * @return key值
     */
    default String fieldKey(ValidInfo validInfo) {
        String validClassName = validInfo.getValidClassName();
        String fieldName = validInfo.getFieldName();
        String simpleName = validClassName.substring(validClassName.lastIndexOf('.') + 1);
        String nodeName = Introspector.decapitalize(simpleName);
        return nodeName + "." + fieldName;
    }

    /**
     * 获取校验字段的key
     *
     * @param validClass 规则约束类的类对象
     * @param fieldName  字段名
     * @return key值
     */
    default String fieldKey(Class<?> validClass, String fieldName) {
        String simpleName = validClass.getSimpleName();
        String nodeName = Introspector.decapitalize(simpleName);
        return nodeName + "." + fieldName;
    }
}
