package info.lostred.ruler.proxy;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import net.sf.cglib.proxy.Enhancer;

/**
 * 默认规则代理
 *
 * @author lostred
 */
public class DefaultRuleProxy extends AbstractRuleProxy {

    public DefaultRuleProxy(AbstractRule abstractRule) {
        super(abstractRule);
    }

    /**
     * 创建一个代理实例对象
     *
     * @param <U> 代理目标的类型
     * @return 代理实例对象
     */
    @SuppressWarnings("unchecked")
    public <U> U newProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (U) enhancer.create(new Class[]{RuleDefinition.class}, new Object[]{null});
    }
}
