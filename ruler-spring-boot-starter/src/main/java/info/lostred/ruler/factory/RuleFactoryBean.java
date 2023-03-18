package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.proxy.RuleProxy;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

import java.lang.reflect.Constructor;

/**
 * 规则工厂bean
 *
 * @author lostred
 * @since 3.2.0
 */
public class RuleFactoryBean implements FactoryBean<AbstractRule>, ApplicationContextAware {
    private final Class<? extends AbstractRule> ruleClass;
    private ApplicationContext applicationContext;

    public RuleFactoryBean(Class<? extends AbstractRule> ruleClass) {
        this.ruleClass = ruleClass;
    }

    @Override
    public AbstractRule getObject() throws Exception {
        Rule rule = ruleClass.getAnnotation(Rule.class);
        RuleDefinition ruleDefinition = RuleDefinition.of(rule, ruleClass);
        Constructor<?> constructor = ruleClass.getDeclaredConstructor();
        Object object = constructor.newInstance();
        AbstractRule abstractRule = (AbstractRule) object;
        abstractRule.setRuleDefinition(ruleDefinition);
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        abstractRule.setExpressionParser(autowireCapableBeanFactory.getBean(ExpressionParser.class));
        abstractRule.setBeanResolver(autowireCapableBeanFactory.getBean(BeanResolver.class));
        autowireCapableBeanFactory.autowireBean(abstractRule);
        abstractRule.init();
        RuleProxy ruleProxy = new RuleProxy(abstractRule);
        return ruleProxy.newProxyInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return ruleClass;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
