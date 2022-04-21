package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.PackageScanUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 应用上下文规则工厂
 *
 * @author lostred
 */
public class AnnotationRuleFactory extends AbstractRuleFactory {
    private final Class<?> configClass;
    private final String[] anotherPackages;

    public AnnotationRuleFactory(Class<?> configClass, String... anotherPackages) {
        this.configClass = configClass;
        this.anotherPackages = anotherPackages;
        this.init();
    }

    @Override
    public void init() {
        String[] mergedPackages;
        Stream<String> mergedStream = Stream.concat(Stream.of("info.lostred.ruler.rule"), Arrays.stream(this.anotherPackages));
        if (this.configClass != null && this.configClass.isAnnotationPresent(RuleScan.class)) {
            String[] scanBasePackages = this.configClass.getAnnotation(RuleScan.class).value();
            mergedPackages = Stream.concat(mergedStream, Stream.of(scanBasePackages))
                    .distinct()
                    .toArray(String[]::new);
        } else {
            mergedPackages = mergedStream.distinct().toArray(String[]::new);
        }
        this.register(mergedPackages);
    }

    /**
     * 从包中注册规则信息与规则
     *
     * @param packages 包名数组
     */
    private void register(String[] packages) {
        for (String packageName : packages) {
            try {
                Set<String> classNames = PackageScanUtils.findClassNames(packageName);
                classNames.stream()
                        .map(PackageScanUtils::loadClass)
                        .filter(Objects::nonNull)
                        .filter(AbstractRule.class::isAssignableFrom)
                        .filter(e -> e.isAnnotationPresent(Rule.class))
                        .map(this::buildRuleInfo)
                        .forEach(this::registerRuleInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (String ruleCode : this.ruleInfoMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleInfoMap.get(ruleCode);
            this.createRule(ruleDefinition);
        }
    }

    /**
     * 构建规则信息
     *
     * @param ruleClass 规则类
     */
    private RuleDefinition buildRuleInfo(Class<?> ruleClass) {
        Rule rule = ruleClass.getAnnotation(Rule.class);
        return RuleDefinition.of(ruleClass, rule);
    }
}
