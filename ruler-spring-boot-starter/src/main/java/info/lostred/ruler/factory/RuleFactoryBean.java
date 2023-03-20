package info.lostred.ruler.factory;

import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.factory.FactoryBean;

/**
 * 规则工厂bean
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleFactoryBean implements FactoryBean<AbstractRule> {
    private final Class<? extends AbstractRule> ruleClass;
    private final RuleFactory ruleFactory;

    public RuleFactoryBean(Class<? extends AbstractRule> ruleClass, RuleFactory ruleFactory) {
        this.ruleClass = ruleClass;
        this.ruleFactory = ruleFactory;
    }

    @Override
    public AbstractRule getObject() {
        return ruleFactory.getRule(ruleClass);
    }

    @Override
    public Class<?> getObjectType() {
        return ruleClass;
    }
}
