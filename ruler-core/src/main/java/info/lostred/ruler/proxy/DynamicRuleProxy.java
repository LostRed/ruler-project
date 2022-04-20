package info.lostred.ruler.proxy;

import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.rule.DynamicRule;
import net.sf.cglib.proxy.Enhancer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 动态规则代理
 *
 * @author lostred
 */
public class DynamicRuleProxy extends AbstractRuleProxy {

    public DynamicRuleProxy(DynamicRule<?, ?> dynamicRule) {
        super(dynamicRule);
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create(new Class[]{RuleInfo.class}, new Object[]{null});
    }

    /**
     * 获取动态规则代理建造者对象
     *
     * @param ruleInfo     规则信息
     * @param validClass   规则约束类的类对象
     * @param nodeClass    节点类的类对象
     * @param isCollection 校验的数据是否是集合
     * @param <E>          规则约束类型
     * @param <Node>       节点类型
     * @return 建造者对象
     */
    public static <E, Node> Builder<E, Node> builder(RuleInfo ruleInfo, Class<E> validClass, Class<Node> nodeClass, Boolean isCollection) {
        return new Builder<>(ruleInfo, isCollection);
    }

    public static class Builder<E, Node> {
        private final DynamicRule<E, Node> dynamicRule;
        private final DynamicRuleProxy dynamicRuleProxy;

        public Builder(RuleInfo ruleInfo, Boolean isCollection) {
            this.dynamicRule = new DynamicRule<>(ruleInfo, isCollection);
            this.dynamicRuleProxy = new DynamicRuleProxy(dynamicRule);
        }

        public Builder<E, Node> setGetNode(Function<E, Node> getNode) {
            dynamicRule.setGetNode(getNode);
            return this;
        }

        public Builder<E, Node> setGetCollection(Function<E, Collection<Node>> getCollection) {
            dynamicRule.setGetCollection(getCollection);
            return this;
        }

        public Builder<E, Node> setIsSupported(Predicate<Node> isSupported) {
            dynamicRule.setIsSupported(isSupported);
            return this;
        }

        public Builder<E, Node> setJudge(Predicate<Node> judge) {
            dynamicRule.setJudge(judge);
            return this;
        }

        public Builder<E, Node> setCollectEntries(Function<Node, Set<Map.Entry<String, Object>>> collectEntries) {
            dynamicRule.setCollectEntries(collectEntries);
            return this;
        }

        @SuppressWarnings("unchecked")
        public DynamicRule<E, Node> build() {
            if (this.dynamicRule.getIsCollection() && this.dynamicRule.getGetCollection() == null) {
                throw new IllegalArgumentException("DynamicRule's getCollection must not be null.");
            } else if (this.dynamicRule.getGetNode() == null) {
                throw new IllegalArgumentException("DynamicRule's getNode must not be null.");
            }
            if (this.dynamicRule.getIsSupported() == null
                    || this.dynamicRule.getJudge() == null
                    || this.dynamicRule.getCollectEntries() == null) {
                throw new IllegalArgumentException("DynamicRule's isSupported, judge, buildReport must not be null.");
            }
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(dynamicRule.getClass());
            enhancer.setCallback(dynamicRuleProxy);
            return (DynamicRule<E, Node>) enhancer.create(new Class[]{RuleInfo.class, Boolean.class}, new Object[]{null, null});
        }
    }
}
