package info.lostred.ruler.factory;

import info.lostred.ruler.util.ClassUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

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
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            AbstractBeanDefinition definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            Class<?> ruleClass = ClassUtils.loadClass(beanClassName);
            definition.setBeanClassName(RuleFactoryBean.class.getName());
            definition.getConstructorArgumentValues().addGenericArgumentValue(ruleClass);
        }
        return holders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isConcrete() && metadata.isIndependent();
    }
}
