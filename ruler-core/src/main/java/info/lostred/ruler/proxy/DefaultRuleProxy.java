package info.lostred.ruler.proxy;

import info.lostred.ruler.core.ValidConfiguration;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.SingleFieldRule;
import net.sf.cglib.proxy.Enhancer;

/**
 * 默认规则代理
 *
 * @author lostred
 */
public class DefaultRuleProxy extends AbstractRuleProxy {

    public DefaultRuleProxy(AbstractRule<?> abstractRule) {
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
        if (target instanceof SingleFieldRule) {
            return (U) enhancer.create(new Class[]{RuleInfo.class, ValidConfiguration.class}, new Object[]{null, null});
        } else {
            return (U) enhancer.create(new Class[]{RuleInfo.class}, new Object[]{null});
        }
    }
}
