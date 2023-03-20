package info.lostred.ruler.factory;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * 类路径规则扫描器
 *
 * @author lostred
 * @since 3.2.0
 */
public class ClassPathAbstractRuleScanner extends ClassPathBeanDefinitionScanner {
    public ClassPathAbstractRuleScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            AbstractBeanDefinition definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            try {
                Class<?> ruleClass = Class.forName(beanClassName);
                definition.setBeanClassName(RuleFactoryBean.class.getName());
                definition.getConstructorArgumentValues().addGenericArgumentValue(ruleClass);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return holders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isConcrete();
    }
}
