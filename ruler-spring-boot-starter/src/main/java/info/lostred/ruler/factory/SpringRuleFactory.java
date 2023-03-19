package info.lostred.ruler.factory;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class SpringRuleFactory extends AbstractRuleFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringRuleFactory(ObjectProvider<AbstractRule> abstractRules) {
        Map<String, RuleDefinition> ruleDefinitionMap = this.getRuleDefinitionMap();
        Map<String, AbstractRule> abstractRuleMap = this.getAbstractRuleMap();
        for (AbstractRule abstractRule : abstractRules) {
            ruleDefinitionMap.put(abstractRule.getRuleDefinition().getRuleCode(), abstractRule.getRuleDefinition());
            abstractRuleMap.put(abstractRule.getRuleDefinition().getRuleCode(), abstractRule);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public AbstractRule getRule(String ruleCode) {
        Map<String, RuleDefinition> ruleDefinitionMap = this.getRuleDefinitionMap();
        RuleDefinition ruleDefinition = ruleDefinitionMap.get(ruleCode);
        if (ruleDefinition == null) {
            throw new RuntimeException("The rule [" + ruleCode + "] is not found in RuleFactory");
        }
        return this.getRule(ruleDefinition.getRuleClass());
    }

    @Override
    public AbstractRule getRule(Class<? extends AbstractRule> ruleClass) {
        return applicationContext.getBean(ruleClass);
    }
}
