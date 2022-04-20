package info.lostred.ruler.rule;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 动态规则
 *
 * @param <E>    规则约束的参数类型
 * @param <Node> 校验的节点类型
 * @author lostred
 */
public class DynamicRule<E, Node> extends AbstractRule<E> {
    /**
     * 校验的数据是否是集合
     */
    private Boolean isCollection;
    /**
     * 获取单个校验节点的函数
     */
    private Function<E, Node> getNode;
    /**
     * 获取多个校验节点的集合的函数
     */
    private Function<E, Collection<Node>> getCollection;
    /**
     * 规则是否支持的断定
     */
    private Predicate<Node> isSupported;
    /**
     * 校验结果为违规的断定
     */
    private Predicate<Node> judge;
    /**
     * 收集非法键值对的函数
     */
    private Function<Node, Set<Map.Entry<String, Object>>> collectEntries;

    public DynamicRule(RuleInfo ruleInfo, Boolean isCollection) {
        super(ruleInfo);
        this.isCollection = isCollection;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }

    @Override
    public boolean isSupported(E object) {
        if (isCollection) {
            Collection<Node> collection = getCollection.apply(object);
            return collection != null && collection.stream().anyMatch(node -> isSupported.test(node));
        } else {
            Node node = getNode.apply(object);
            return node != null && isSupported.test(node);
        }
    }

    @Override
    public boolean judge(E object) {
        if (isCollection) {
            Collection<Node> collection = getCollection.apply(object);
            return collection.stream().anyMatch(node -> judge.test(node));
        } else {
            Node node = getNode.apply(object);
            return judge.test(node);
        }
    }

    @Override
    public Report buildReport(E object) {
        Set<Map.Entry<String, Object>> entries;
        if (isCollection) {
            Collection<Node> collection = getCollection.apply(object);
            entries = collection.stream()
                    .filter(Objects::nonNull)
                    .flatMap(node -> collectEntries.apply(node).stream())
                    .collect(Collectors.toSet());
        } else {
            Node node = getNode.apply(object);
            if (node != null) {
                entries = collectEntries.apply(node);
            } else {
                entries = new HashSet<>(0);
            }
        }
        return Report.of(ruleInfo).putIllegals(entries);
    }

    public boolean getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public Function<E, Node> getGetNode() {
        return getNode;
    }

    public void setGetNode(Function<E, Node> getNode) {
        this.getNode = getNode;
    }

    public Function<E, Collection<Node>> getGetCollection() {
        return getCollection;
    }

    public void setGetCollection(Function<E, Collection<Node>> getCollection) {
        this.getCollection = getCollection;
    }

    public Predicate<Node> getIsSupported() {
        return isSupported;
    }

    public void setIsSupported(Predicate<Node> isSupported) {
        this.isSupported = isSupported;
    }

    public Predicate<Node> getJudge() {
        return judge;
    }

    public void setJudge(Predicate<Node> judge) {
        this.judge = judge;
    }

    public Function<Node, Set<Map.Entry<String, Object>>> getCollectEntries() {
        return collectEntries;
    }

    public void setCollectEntries(Function<Node, Set<Map.Entry<String, Object>>> collectEntries) {
        this.collectEntries = collectEntries;
    }
}
