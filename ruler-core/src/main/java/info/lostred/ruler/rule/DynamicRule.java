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
 * @param <T> 规则约束的参数类型
 * @param <E> 校验的节点类型
 * @author lostred
 */
public class DynamicRule<T, E> extends AbstractRule<T> {
    /**
     * 校验的数据是否是集合
     */
    private Boolean isCollection;
    /**
     * 获取单个校验节点的函数
     */
    private Function<T, E> getNode;
    /**
     * 获取多个校验节点的集合的函数
     */
    private Function<T, Collection<E>> getCollection;
    /**
     * 规则是否支持的断定
     */
    private Predicate<E> isSupported;
    /**
     * 校验结果为违规的断定
     */
    private Predicate<E> judge;
    /**
     * 收集非法键值对的函数
     */
    private Function<E, Set<Map.Entry<String, Object>>> collectEntries;

    public DynamicRule(RuleInfo ruleInfo, Boolean isCollection) {
        super(ruleInfo);
        this.isCollection = isCollection;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }

    @Override
    public boolean isSupported(T object) {
        if (isCollection) {
            Collection<E> collection = getCollection.apply(object);
            return collection != null && collection.stream().anyMatch(e -> isSupported.test(e));
        } else {
            E e = getNode.apply(object);
            return e != null && isSupported.test(e);
        }
    }

    @Override
    public boolean judge(T object) {
        if (isCollection) {
            Collection<E> collection = getCollection.apply(object);
            return collection.stream().anyMatch(e -> judge.test(e));
        } else {
            E e = getNode.apply(object);
            return judge.test(e);
        }
    }

    @Override
    public Report buildReport(T object) {
        Set<Map.Entry<String, Object>> entries;
        if (isCollection) {
            Collection<E> collection = getCollection.apply(object);
            entries = collection.stream()
                    .filter(Objects::nonNull)
                    .flatMap(e -> collectEntries.apply(e).stream())
                    .collect(Collectors.toSet());
        } else {
            E e = getNode.apply(object);
            if (e != null) {
                entries = collectEntries.apply(e);
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

    public Function<T, E> getGetNode() {
        return getNode;
    }

    public void setGetNode(Function<T, E> getNode) {
        this.getNode = getNode;
    }

    public Function<T, Collection<E>> getGetCollection() {
        return getCollection;
    }

    public void setGetCollection(Function<T, Collection<E>> getCollection) {
        this.getCollection = getCollection;
    }

    public Predicate<E> getIsSupported() {
        return isSupported;
    }

    public void setIsSupported(Predicate<E> isSupported) {
        this.isSupported = isSupported;
    }

    public Predicate<E> getJudge() {
        return judge;
    }

    public void setJudge(Predicate<E> judge) {
        this.judge = judge;
    }

    public Function<E, Set<Map.Entry<String, Object>>> getCollectEntries() {
        return collectEntries;
    }

    public void setCollectEntries(Function<E, Set<Map.Entry<String, Object>>> collectEntries) {
        this.collectEntries = collectEntries;
    }
}
