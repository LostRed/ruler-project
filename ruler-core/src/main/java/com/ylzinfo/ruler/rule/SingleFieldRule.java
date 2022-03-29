package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
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
    }

    /**
     * 收集非法字段与值
     *
     * @param element   规则约束的对象
     * @param validInfo 校验信息
     * @return 非法字段与值
     */
    protected Set<Map.Entry<String, Object>> collectIllegals(E element, ValidInfo validInfo) {
        Object validNode = this.findValidNode(element, validInfo.getValidClass());
        if (validNode instanceof Collection) {
            return ((Collection<?>) validNode).stream()
                    .flatMap(node -> this.collectFromValidNode(node, validInfo).stream())
                    .collect(Collectors.toSet());
        } else {
            return this.collectFromValidNode(validNode, validInfo);
        }
    }

    /**
     * 找到校验的节点
     *
     * @param validRootNode 校验根节点
     * @param validClass    规则约束类的类对象
     * @return 待校验节点的值
     */
    protected Object findValidNode(Object validRootNode, Class<?> validClass) {
        if (validRootNode.getClass() == validClass) {
            return validRootNode;
        } else {
            return this.findValidNodeFromField(validRootNode, validClass);
        }
    }

    /**
     * 从成员变量中寻找校验的节点
     *
     * @param validNode  校验节点
     * @param validClass 规则约束类的类对象
     * @return 待校验节点的值
     */
    protected Object findValidNodeFromField(Object validNode, Class<?> validClass) {
        Field[] fields = validNode.getClass().getDeclaredFields();
        Field nodeField = null;
        for (Field field : fields) {
            //属性为对象且是校验类时
            if (field.getType() == validClass) {
                nodeField = field;
            }
            //属性为数组时
            else if (field.getType() == Collection.class
                    || field.getType() == List.class
                    || field.getType() == Set.class) {
                Type type = field.getGenericType();
                if (type instanceof ParameterizedType) {
                    Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                    if (actualTypeArguments.length == 1) {
                        Type actualTypeArgument = actualTypeArguments[0];
                        if (actualTypeArgument.getTypeName().equals(validClass.getTypeName())) {
                            nodeField = field;
                        }
                    }
                }
            }
            //属性为对象且是非基本数据类型时
            else if (this.isNotBaseType(field.getType())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(validNode);
                    return this.findValidNodeFromField(value, validClass);
                } catch (IllegalAccessException ignored) {
                }
            }
            if (nodeField != null) {
                nodeField.setAccessible(true);
                try {
                    return nodeField.get(validNode);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 不是非基本数据类型
     *
     * @param type 类型
     * @return 不是返回true，否则返回false
     */
    private boolean isNotBaseType(Class<?> type) {
        return !String.class.equals(type)
                && !Temporal.class.isAssignableFrom(type)
                && !Date.class.isAssignableFrom(type)
                && !Number.class.isAssignableFrom(type);
    }

    /**
     * 从校验节点中找到待校验的成员变量
     *
     * @param child     子类
     * @param fieldName 字段名
     * @return 待校验的成员变量
     */
    protected Field findField(Class<?> child, String fieldName) {
        if (child == null) {
            return null;
        }
        try {
            return child.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
            Class<?> parent = child.getSuperclass();
            return this.findField(parent, fieldName);
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
        Field field = this.findField(validNode.getClass(), validInfo.getFieldName());
        if (field != null) {
            field.setAccessible(true);
            try {
                return this.isNotMatch(validInfo, field.get(validNode));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 从校验对象数据收集非法的字段与值
     *
     * @param validNode 校验节点
     * @param validInfo 校验信息
     * @return 非法的字段与值
     */
    protected Set<Map.Entry<String, Object>> collectFromValidNode(Object validNode, ValidInfo validInfo) {
        if (validNode != null) {
            Field field = this.findField(validNode.getClass(), validInfo.getFieldName());
            if (field != null) {
                field.setAccessible(true);
                try {
                    Object value = field.get(validNode);
                    if (this.isNotMatch(validInfo, value)) {
                        return this.wrap(validInfo, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
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
     * @param validInfo 校验信息
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    protected Set<Map.Entry<String, Object>> wrap(ValidInfo validInfo, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(this.fieldKey(validInfo), value);
        return map.entrySet();
    }
}
