package info.lostred.ruler.factory;

import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * 规则工厂bean
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleFactoryBean implements FactoryBean<AbstractRule>, BeanFactoryAware {
    private final Class<? extends AbstractRule> ruleClass;
    private RuleFactory ruleFactory;

    public RuleFactoryBean(Class<? extends AbstractRule> ruleClass) {
        this.ruleClass = ruleClass;
    }

    @Override
    public AbstractRule getObject() {
        return ruleFactory.getRule(ruleClass);
    }

    @Override
    public Class<?> getObjectType() {
        return ruleClass;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.ruleFactory = beanFactory.getBean(RuleFactory.class);
    }
}
