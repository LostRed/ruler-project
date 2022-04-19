package info.lostred.ruler.rule;

import info.lostred.ruler.core.AbstractRule;
import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.NodeInfo;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.util.ReflectUtils;

import java.text.AttributedString;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        NodeInfo nodeInfo = ReflectUtils.searchAndGetNodeByType(element, validInfo.getValidClass());
        Object validNode = nodeInfo.getNode();
        if (validNode instanceof Collection) {
            return ((Collection<?>) validNode).stream()
                    .flatMap(node -> this.streamEntryFromCollection(nodeInfo, validInfo, node))
                    .collect(Collectors.toSet());
        } else {
            return this.collectEntrySetFromObject(nodeInfo, validInfo);
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
        NodeInfo nodeInfo = new NodeInfo(validNode);
        Object value = ReflectUtils.searchAndGetValueByName(nodeInfo, validInfo.getFieldName());
        return this.isIllegal(validInfo, value);
    }

    /**
     * 从集合类节点收集非法的字段与值并转换为流
     *
     * @param nodeInfo  集合的节点信息
     * @param validInfo 校验信息
     * @param element   集合中的元素
     * @return 键值对的流
     */
    protected Stream<Map.Entry<String, Object>> streamEntryFromCollection(NodeInfo nodeInfo, ValidInfo validInfo, Object element) {
        NodeInfo subNodeInfo = ReflectUtils.getNodeInfoFromCollection(nodeInfo, element);
        return this.collectEntrySetFromObject(subNodeInfo, validInfo).stream();
    }

    /**
     * 从对象类节点收集非法的字段与值
     *
     * @param nodeInfo  节点信息
     * @param validInfo 校验信息
     * @return 非法的字段与值
     */
    protected Set<Map.Entry<String, Object>> collectEntrySetFromObject(NodeInfo nodeInfo, ValidInfo validInfo) {
        NodeInfo newNodeInfo = ReflectUtils.searchAndGetValueByName(nodeInfo, validInfo.getFieldName());
        if (this.isIllegal(validInfo, newNodeInfo.getNode())) {
            return this.wrapToSet(validInfo, newNodeInfo.getNodeTrace(), newNodeInfo.getNode());
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
     * @param validInfo 校验信息
     * @param nodeTrace 节点链路
     * @param value     违规值
     * @return 非法字段与值的集合
     */
    protected Set<Map.Entry<String, Object>> wrapToSet(ValidInfo validInfo, String nodeTrace, Object value) {
        Map<String, Object> map = new HashMap<>();
        if ("".equals(nodeTrace)) {
            map.put(validInfo.getFieldName(), value);
        } else {
            map.put(nodeTrace, value);
        }
        return map.entrySet();
    }
}
