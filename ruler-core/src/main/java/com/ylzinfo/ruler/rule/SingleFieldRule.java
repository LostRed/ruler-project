package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

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
     * @param t         约束的参数对象
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean check(E t, ValidInfo validInfo) {
        try {
            Object validNode = this.findValidNode(t, validInfo.getValidClass());
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
            Object validNode = this.findValidNode(element, validInfo.getValidClass());
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
     * 找到校验的节点
     *
     * @param validNode  校验节点
     * @param validClass 规则约束类的类对象
     * @return 待校验节点的值
     * @throws IllegalAccessException 无法获取字段的值
     * @throws NoSuchFieldException   无法在类中找到字段
     */
    protected Object findValidNode(Object validNode, Class<?> validClass) throws IllegalAccessException, NoSuchFieldException {
        if (validNode.getClass() == validClass) {
            return validNode;
        } else {
            Field[] fields = validNode.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                //属性为对象且是校验类时
                if (field.getType() == validClass) {
                    return field.get(validNode);
                }
                //属性为数组时
                else if (Collection.class.isAssignableFrom(field.getType())) {
                    if (this.includeValidClass(field, validClass)) {
                        return field.get(validNode);
                    }
                }
                //属性为对象且是非基本数据类型时
                else if (this.isNotBaseType(field.getType())) {
                    try {
                        return this.findValidNode(field.get(validNode), validClass);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
            throw new NoSuchFieldException("Cannot find the validClass in this validNode.");
        }
    }

    /**
     * 从校验节点的类对象中找到待校验的成员变量
     *
     * @param child     子类
     * @param fieldName 字段名
     * @return 待校验的成员变量
     * @throws NoSuchFieldException 无法在类中找到字段
     */
    protected Field findField(Class<?> child, String fieldName) throws NoSuchFieldException {
        try {
            return child.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
            Class<?> parent = child.getSuperclass();
            return this.findField(parent, fieldName);
        } catch (NullPointerException ignored) {
            throw new NoSuchFieldException("Cannot find this fieldName.");
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
            Field field = this.findField(validNode.getClass(), validInfo.getFieldName());
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
            Field field = this.findField(validNode.getClass(), validInfo.getFieldName());
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
        String fieldTrace = this.fieldTrace(element.getClass(), validInfo.getValidClass(),
                validInfo.getFieldName(), value, "");
        map.put(fieldTrace, value);
        return map.entrySet();
    }
}
