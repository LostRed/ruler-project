package info.lostred.ruler.factory;

import info.lostred.ruler.builder.RuleBuilder;
import info.lostred.ruler.builder.RuleDefinitionBuilder;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;

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
    public AbstractRule getObject() {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        RuleDefinition ruleDefinition = RuleDefinitionBuilder.build(ruleClass).getRuleDefinition();
        RuleBuilder builder = RuleBuilder.build(ruleDefinition)
                .expressionParser(autowireCapableBeanFactory.getBean(ExpressionParser.class));
        autowireCapableBeanFactory.autowireBean(builder.getRawRule());
        return builder.getProxyRule();
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
