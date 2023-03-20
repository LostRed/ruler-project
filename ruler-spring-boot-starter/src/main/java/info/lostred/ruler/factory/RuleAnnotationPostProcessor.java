package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Rule注解后置处理器
 *
 * @author lostred
 * @since 3.2.3
 */
public class RuleAnnotationPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private final static String RULE_SCAN_PACKAGES_KEY = "ruler.rule-scan-packages";
    private Environment environment;
    private String[] basePackages;

    public RuleAnnotationPostProcessor(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] basePackages = Stream.concat(Arrays.stream(this.basePackages),
                        Arrays.stream(StringUtils.commaDelimitedListToStringArray(environment.getProperty(RULE_SCAN_PACKAGES_KEY))))
                .filter(StringUtils::hasText)
                .distinct()
                .toArray(String[]::new);
        ClassPathAbstractRuleScanner scanner = new ClassPathAbstractRuleScanner(registry);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Rule.class));
        // 扫描@Rule注解的类并注册BeanDefinition
        scanner.scan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
