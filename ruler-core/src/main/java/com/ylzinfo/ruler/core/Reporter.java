package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.beans.Introspector;
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
     * @param validNode 校验节点
     * @param fieldName 字段名
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    default Report getReport(RuleInfo ruleInfo, Object validNode, String fieldName, Object value) {
        Map<String, Object> map = new HashMap<>();
        String simpleName = validNode.getClass().getSimpleName();
        String nodeName = Introspector.decapitalize(simpleName);
        map.put(nodeName + "." + fieldName, value);
        return Report.of(ruleInfo).putIllegal(map);
    }

    /**
     * 将非法字段与值的集合包装成报告
     *
     * @param illegals 非法字段与值的集合
     * @return 非法字段与值的集合
     */
    default Report getReport(RuleInfo ruleInfo, Map<String, Object> illegals) {
        return Report.of(ruleInfo).putIllegal(illegals);
    }
}
