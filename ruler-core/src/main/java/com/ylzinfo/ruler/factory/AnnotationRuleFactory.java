package com.ylzinfo.ruler.factory;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.annotation.RuleScan;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.RuleInfo;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 应用上下文规则工厂
 *
 * @author dengluwei
 */
public class AnnotationRuleFactory extends AbstractRuleFactory {
    private final Class<?> configClass;
    private final String[] anotherPackages;

    public AnnotationRuleFactory(ValidConfiguration validConfiguration, Class<?> configClass, String... anotherPackages) {
        super(validConfiguration);
        this.configClass = configClass;
        this.anotherPackages = anotherPackages;
        this.init();
    }

    /**
     * 获取类文件
     *
     * @param resource 资源
     * @return 文件数组
     */
    private File[] getClassFiles(URL resource) {
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    /**
     * 注册规则信息
     *
     * @param ruleClass 规则信息
     */
    private void registerRuleInfo(Class<?> ruleClass) {
        if (ruleClass.isAnnotationPresent(Rule.class)) {
            Rule rule = ruleClass.getAnnotation(Rule.class);
            RuleInfo ruleInfo = new RuleInfo();
            ruleInfo.setRuleCode(rule.ruleCode());
            ruleInfo.setBusinessType(rule.businessType());
            ruleInfo.setGrade(rule.validGrade().getText());
            ruleInfo.setDesc(rule.desc());
            ruleInfo.setSeq(rule.seq());
            ruleInfo.setRequired(rule.required());
            ruleInfo.setEnable(rule.enable());
            ruleInfo.setRuleClassName(ruleClass.getName());
            ruleInfo.setValidClass(rule.validClass());
            ruleInfo.setValidClassName(rule.validClass().getName());
            if (ruleInfoMap.containsKey(ruleInfo.getRuleCode())) {
                throw new RuntimeException("Rule code '" + ruleInfo.getRuleCode() + "' is repeat.");
            }
            ruleInfoMap.put(ruleInfo.getRuleCode(), ruleInfo);
        }
    }

    @Override
    public void init() {
        Stream<String> stream = Stream.concat(Stream.of("com.ylzinfo.ruler.rule"), Arrays.stream(this.anotherPackages));
        if (this.configClass != null && this.configClass.isAnnotationPresent(RuleScan.class)) {
            String[] scanBasePackages = this.configClass.getAnnotation(RuleScan.class).value();
            String[] packages = Stream.concat(stream, Stream.of(scanBasePackages)).distinct().toArray(String[]::new);
            this.register(packages);
        } else {
            this.register(stream.distinct().toArray(String[]::new));
        }
    }

    /**
     * 从包中注册规则信息与规则
     *
     * @param packages 包名数组
     */
    private void register(String[] packages) {
        for (String packageName : packages) {
            String packagePath = packageName.replace(".", "/");
            ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(packagePath);
            assert resource != null;
            File[] classFiles = this.getClassFiles(resource);
            assert classFiles != null;
            for (File file : classFiles) {
                String absolutePath = file.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String fileName = file.getName();
                    String relativePath = packagePath + "/" + fileName.substring(0, fileName.lastIndexOf("."));
                    String className = relativePath.replace("/", ".");
                    try {
                        Class<?> ruleClass = classLoader.loadClass(className);
                        this.registerRuleInfo(ruleClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (String ruleCode : this.ruleInfoMap.keySet()) {
            RuleInfo ruleInfo = this.ruleInfoMap.get(ruleCode);
            Class<?> validClass = ruleInfo.getValidClass();
            AbstractRule<?> rule = builder(validConfiguration, ruleInfo, validClass).build();
            this.rules.put(ruleCode, rule);
        }
    }
}
