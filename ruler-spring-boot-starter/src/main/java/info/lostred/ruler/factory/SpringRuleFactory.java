package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.DeclarativeRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring规则工厂
 *
 * @author lostred
 * @since 3.2.3
 */
public class SpringRuleFactory extends AbstractRuleFactory implements ApplicationContextAware {
    private AutowireCapableBeanFactory beanFactory;

    @Override
    protected AbstractRule createRule(String ruleCode, RuleDefinition ruleDefinition) {
        Class<? extends AbstractRule> ruleClass = ruleDefinition.getRuleClass();
        AbstractRule abstractRule = beanFactory.createBean(ruleClass);
        abstractRule.setRuleDefinition(ruleDefinition);
        if (abstractRule instanceof DeclarativeRule) {
            beanFactory.autowireBeanProperties(abstractRule, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        }
        abstractRule.init();
        return abstractRule;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }
}
