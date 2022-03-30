package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
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
        String fieldTrace = this.fieldTrace(element.getClass(), element.getClass(), fieldName, value, "");
        map.put(fieldTrace, value);
        return Report.of(ruleInfo).putIllegal(map);
    }

    /**
     * 获取校验字段的链路
     *
     * @param validNodeClass 校验节点的类对象
     * @param validClass     规则约束类的类对象
     * @param fieldName      字段名
     * @param value          违规值
     * @param fieldTrace     字段链路
     * @return 校验字段的链路
     */
    default String fieldTrace(Class<?> validNodeClass, Class<?> validClass,
                              String fieldName, Object value, String fieldTrace) {
        StringBuilder sb = new StringBuilder(fieldTrace);
        if (validNodeClass.equals(validClass)) {
            sb.append(fieldName);
            return sb.toString();
        } else {
            Field[] fields = validNodeClass.getDeclaredFields();
            for (Field field : fields) {
                //属性为数组时
                if (Collection.class.isAssignableFrom(field.getType())) {
                    if (this.includeValidClass(field, validClass)) {
                        field.setAccessible(true);
                        sb.append(field.getName())
                                .append("[").append(value.hashCode()).append("]")
                                .append(".").append(fieldName);
                        return sb.toString();
                    }
                }
                //属性为对象且是非基本数据类型时
                else if (this.isNotBaseType(field.getType())) {
                    sb.append(field.getName());
                    String subFieldTrace = this.fieldTrace(field.getType(), validClass, fieldName, value, sb.append(".").toString());
                    if (subFieldTrace != null) {
                        return subFieldTrace;
                    }
                }
            }
            return null;
        }
    }

    /**
     * 字段中是否有包含规则约束类的字段
     *
     * @param field      字段
     * @param validClass 规则约束类的类对象
     * @return 有返回true，否则返回false
     */
    default boolean includeValidClass(Field field, Class<?> validClass) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                Type actualTypeArgument = actualTypeArguments[0];
                return actualTypeArgument.getTypeName().equals(validClass.getTypeName());
            }
        }
        return false;
    }

    /**
     * 是否是非基本数据类型
     *
     * @param type 类型
     * @return 是返回true，否则返回false
     */
    default boolean isNotBaseType(Class<?> type) {
        return !String.class.equals(type)
                && !Temporal.class.isAssignableFrom(type)
                && !Date.class.isAssignableFrom(type)
                && !Number.class.isAssignableFrom(type);
    }
}
