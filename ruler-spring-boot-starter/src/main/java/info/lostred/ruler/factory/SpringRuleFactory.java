package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring规则工厂
 *
 * @author lostred
 * @since 3.2.3
 */
public class SpringRuleFactory extends AbstractRuleFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringRuleFactory(Iterable<RuleDefinition> ruleDefinitions) {
        super(ruleDefinitions);
    }

    @Override
    protected synchronized AbstractRule createRule(String ruleCode, RuleDefinition ruleDefinition) {
        AbstractRule abstractRule = super.createRule(ruleCode, ruleDefinition);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(abstractRule);
        return abstractRule;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
