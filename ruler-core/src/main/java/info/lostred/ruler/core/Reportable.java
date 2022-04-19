package info.lostred.ruler.core;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.util.ReflectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 可生成报告接口
 *
 * @param <E> 报告数据来源的参数类型
 * @author lostred
 */
public interface Reportable<E> {
    /**
     * 生成校验报告
     *
     * @param object 参数
     * @return 报告
     */
    Report buildReport(E object);

    /**
     * 获取非法字段与值的键值对
     *
     * @param nodeTrace 节点链路
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    default Set<Map.Entry<String, Object>> getEntry(String nodeTrace, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(nodeTrace, value);
        return map.entrySet();
    }

    /**
     * 从集合中获取非法字段与值
     *
     * @param nodeTrace 集合节点的链路
     * @param element   集合元素节点
     * @param fieldName 集合元素的某个字段名
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    default Set<Map.Entry<String, Object>> getEntryFromCollection(String nodeTrace, Object element, String fieldName, Object value) {
        nodeTrace = ReflectUtils.concatNodeTrace(nodeTrace, element, fieldName);
        return this.getEntry(nodeTrace, value);
    }
}
