package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public abstract class SingleFieldRule<E> extends AbstractRule<E> {

    public SingleFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    /**
     * 检查是否违规
     *
     * @param element   约束的参数对象
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean check(E element, ValidInfo validInfo) {
        try {
            Object validNode = ReflectUtils.findFieldValueByType(element, validInfo.getValidClass());
            //校验数组属性
            if (validNode instanceof Collection) {
                return ((Collection<?>) validNode)
                        .stream().anyMatch(e -> this.valid(e, validInfo));
            }
            //校验对象属性
            else {
                return this.valid(validNode, validInfo);
            }
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            return false;
        }
    }

    /**
     * 收集非法字段与值
     *
     * @param element   规则约束的对象
     * @param validInfo 校验信息
     * @return 非法字段与值
     */
    protected Set<Map.Entry<String, Object>> collectIllegals(E element, ValidInfo validInfo) {
        try {
            Object validNode = ReflectUtils.findFieldValueByType(element, validInfo.getValidClass());
            if (validNode instanceof Collection) {
                return ((Collection<?>) validNode).stream()
                        .flatMap(node -> this.collectFromValidNode(element, node, validInfo).stream())
                        .collect(Collectors.toSet());
            } else {
                return this.collectFromValidNode(element, validNode, validInfo);
            }
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            return new HashSet<>();
        }
    }

    /**
     * 校验对象数据
     *
     * @param validNode 校验节点
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean valid(Object validNode, ValidInfo validInfo) {
        try {
            Field field = ReflectUtils.findFieldByName(validNode.getClass(), validInfo.getFieldName());
            field.setAccessible(true);
            return this.isNotMatch(validInfo, field.get(validNode));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return false;
        }
    }

    /**
     * 从校验对象数据收集非法的字段与值
     *
     * @param element   规则约束的对象
     * @param validNode 校验节点
     * @param validInfo 校验信息
     * @return 非法的字段与值
     */
    protected Set<Map.Entry<String, Object>> collectFromValidNode(E element, Object validNode, ValidInfo validInfo) {
        try {
            Field field = ReflectUtils.findFieldByName(validNode.getClass(), validInfo.getFieldName());
            field.setAccessible(true);
            Object value = field.get(validNode);
            if (this.isNotMatch(validInfo, value)) {
                return this.wrap(element, validInfo, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return new HashSet<>();
    }

    /**
     * 是否不匹配规则逻辑
     *
     * @param validInfo 校验信息
     * @param value     待校验的值
     * @return 不匹配返回true，否则返回false
     */
    protected abstract boolean isNotMatch(ValidInfo validInfo, Object value);

    /**
     * 将非法字段与值包装成集合
     *
     * @param element   规则约束的对象
     * @param validInfo 校验信息
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    protected Set<Map.Entry<String, Object>> wrap(E element, ValidInfo validInfo, Object value) {
        Map<String, Object> map = new HashMap<>();
        try {
            String fieldTrace = ReflectUtils.getFieldTrace(element.getClass(), validInfo.getValidClass(),
                    validInfo.getFieldName(), value);
            map.put(fieldTrace, value);
        } catch (NoSuchFieldException e) {
            map.put(validInfo.getFieldName(), value);
        }
        return map.entrySet();
    }
}
