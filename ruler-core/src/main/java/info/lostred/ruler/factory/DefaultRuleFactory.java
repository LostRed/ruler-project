package info.lostred.ruler.factory;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.util.PackageScanUtils;
import org.springframework.expression.ExpressionParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 默认的规则工厂
 *
 * @author lostred
 */
public class DefaultRuleFactory extends AbstractRuleFactory {
    private final ExpressionParser parser;
    private final Class<?> configClass;
    private final String[] anotherPackages;

    public DefaultRuleFactory(ExpressionParser parser, Class<?> configClass, String... anotherPackages) {
        this.parser = parser;
        this.configClass = configClass;
        this.anotherPackages = anotherPackages;
        this.init();
    }

    @Override
    public void init() {
        String[] mergedPackages;
        Stream<String> stream = Arrays.stream(this.anotherPackages);
        if (this.configClass != null && this.configClass.isAnnotationPresent(RuleScan.class)) {
            String[] scanBasePackages = this.configClass.getAnnotation(RuleScan.class).value();
            mergedPackages = Stream.concat(stream, Stream.of(scanBasePackages))
                    .distinct()
                    .toArray(String[]::new);
        } else {
            mergedPackages = stream.distinct().toArray(String[]::new);
        }
        this.registerFromPackages(mergedPackages);
    }

    /**
     * 从包中注册规则信息与规则
     *
     * @param packages 包名数组
     */
    private void registerFromPackages(String[] packages) {
        for (String packageName : packages) {
            try {
                Set<String> classNames = PackageScanUtils.findClassNames(packageName);
                classNames.stream()
                        .map(PackageScanUtils::loadClass)
                        .filter(Objects::nonNull)
                        .filter(AbstractRule.class::isAssignableFrom)
                        .filter(e -> e.isAnnotationPresent(Rule.class))
                        .map(this::buildRuleDefinition)
                        .forEach(this::register);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (String ruleCode : this.ruleInfoMap.keySet()) {
            RuleDefinition ruleDefinition = this.ruleInfoMap.get(ruleCode);
            this.createRule(ruleDefinition, parser);
        }
    }

    /**
     * 构建规则定义
     *
     * @param ruleClass 规则类的类对象
     */
    private RuleDefinition buildRuleDefinition(Class<?> ruleClass) {
        Rule rule = ruleClass.getAnnotation(Rule.class);
        return RuleDefinition.of(rule, ruleClass);
    }
}
