package info.lostred.ruler.rule;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.NodeInfo;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.util.ReflectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public abstract class SingleFieldRule<E> extends AbstractRule<E> {
    /**
     * 校验配置
     */
    protected final ValidConfiguration validConfiguration;

    public SingleFieldRule(RuleInfo ruleInfo, ValidConfiguration validConfiguration) {
        super(ruleInfo);
        this.validConfiguration = validConfiguration;
    }

    /**
     * 检查是否违规
     *
     * @param element   约束的参数对象
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean check(E element, ValidInfo validInfo) {
        NodeInfo nodeInfo = ReflectUtils.searchAndGetNodeByType(element, validInfo.getValidClass());
        Object node = nodeInfo.getNode();
        if (node instanceof Collection) {
            return ((Collection<?>) node).stream()
                    .anyMatch(e -> this.validCollection(nodeInfo, validInfo, e));
        } else {
            return this.valid(nodeInfo, validInfo);
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
        NodeInfo nodeInfo = ReflectUtils.searchAndGetNodeByType(element, validInfo.getValidClass());
        Object node = nodeInfo.getNode();
        if (node instanceof Collection) {
            return ((Collection<?>) node).stream()
                    .flatMap(e -> this.collectEntryFromCollection(nodeInfo, validInfo, e).stream())
                    .collect(Collectors.toSet());
        } else {
            return this.collectEntryFromObject(nodeInfo, validInfo);
        }
    }

    //simple rules engine will invoke these methods below.

    /**
     * 校验集合中的数据
     *
     * @param nodeInfo  集合的节点信息
     * @param validInfo 校验信息
     * @param element   集合中的元素
     * @return 违规返回true，否则返回false
     */
    protected boolean validCollection(NodeInfo nodeInfo, ValidInfo validInfo, Object element) {
        NodeInfo subNodeInfo = ReflectUtils.getNodeInfoFromCollection(nodeInfo, element);
        return this.valid(subNodeInfo, validInfo);
    }

    /**
     * 校验对象数据
     *
     * @param nodeInfo  节点信息
     * @param validInfo 校验信息
     * @return 违规返回true，否则返回false
     */
    protected boolean valid(NodeInfo nodeInfo, ValidInfo validInfo) {
        NodeInfo subNodeInfo = ReflectUtils.searchAndGetValueByName(nodeInfo, validInfo.getFieldName());
        return this.isIllegal(validInfo, subNodeInfo);
    }

    //detail rules engine will invoke these methods below.

    /**
     * 从集合类节点收集非法的字段与值
     *
     * @param nodeInfo  集合的节点信息
     * @param validInfo 校验信息
     * @param element   集合中的元素
     * @return 键值对的流
     */
    protected Set<Map.Entry<String, Object>> collectEntryFromCollection(NodeInfo nodeInfo, ValidInfo validInfo, Object element) {
        NodeInfo subNodeInfo = ReflectUtils.getNodeInfoFromCollection(nodeInfo, element);
        return this.collectEntryFromObject(subNodeInfo, validInfo);
    }

    /**
     * 从对象类节点收集非法的字段与值
     *
     * @param nodeInfo  节点信息
     * @param validInfo 校验信息
     * @return 非法的字段与值
     */
    protected Set<Map.Entry<String, Object>> collectEntryFromObject(NodeInfo nodeInfo, ValidInfo validInfo) {
        NodeInfo subNodeInfo = ReflectUtils.searchAndGetValueByName(nodeInfo, validInfo.getFieldName());
        if (this.isIllegal(validInfo, subNodeInfo.getNode())) {
            return this.collect(validInfo, subNodeInfo.getTrace(), subNodeInfo.getNode());
        } else {
            return new HashSet<>(0);
        }
    }

    //methods to handle result.

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
     * @param validInfo 校验信息
     * @param nodeTrace 节点链路
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    protected Set<Map.Entry<String, Object>> collect(ValidInfo validInfo, String nodeTrace, Object value) {
        if (nodeTrace == null || "".equals(nodeTrace)) {
            return this.getEntry(validInfo.getFieldName(), value);
        }
        return this.getEntry(nodeTrace, value);
    }
}
