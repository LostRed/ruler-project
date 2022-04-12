package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.GlobalConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.util.ReflectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public abstract class SingleFieldRule<E> extends AbstractRule<E> {

    public SingleFieldRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
    }

    /**
     * 检查是否违规
     *
     * @param element   约束的参数对象
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean check(E element, ValidInfo validInfo) {
        Object validNode = ReflectUtils.searchAndGetNodeByType(element, validInfo.getValidClass());
        if (validNode instanceof Collection) {
            return ((Collection<?>) validNode)
                    .stream().anyMatch(e -> this.valid(e, validInfo));
        } else {
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
        Object validNode = ReflectUtils.searchAndGetNodeByType(element, validInfo.getValidClass());
        if (validNode instanceof Collection) {
            return ((Collection<?>) validNode).stream()
                    .flatMap(node -> this.collectFromValidNode(element, node, validInfo).stream())
                    .collect(Collectors.toSet());
        } else {
            return this.collectFromValidNode(element, validNode, validInfo);
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
        Object value = ReflectUtils.searchAndGetValueByName(validNode, validInfo.getFieldName());
        return this.isIllegal(validInfo, value);
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
        Object value = ReflectUtils.searchAndGetValueByName(validNode, validInfo.getFieldName());
        if (this.isIllegal(validInfo, value)) {
            return this.wrap(element, validInfo, value);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * 待校验的值是否是非法的
     *
     * @param validInfo 校验信息
     * @param value     待校验的值
     * @return 是返回true，否则返回false
     */
    protected abstract boolean isIllegal(ValidInfo validInfo, Object value);

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
