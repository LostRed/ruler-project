package info.lostred.ruler.engine;

import info.lostred.ruler.core.RulerContextHolder;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import info.lostred.ruler.rule.SimpleRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 抽象规则引擎
 *
 * @author lostred
 */
public abstract class AbstractRulesEngine implements RulesEngine {
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 规则工厂
     */
    private RuleFactory ruleFactory;
    /**
     * bean解析器
     */
    private BeanResolver beanResolver;
    /**
     * 全局函数
     */
    private List<Method> globalFunctions;
    /**
     * 规则引擎中的规则集合
     */
    protected final List<AbstractRule> rules = new CopyOnWriteArrayList<>();

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public RuleFactory getRuleFactory() {
        return ruleFactory;
    }

    public void setRuleFactory(RuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    public BeanResolver getBeanResolver() {
        return beanResolver;
    }

    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    public List<Method> getGlobalFunctions() {
        return globalFunctions;
    }

    public void setGlobalFunctions(List<Method> globalFunctions) {
        this.globalFunctions = globalFunctions;
    }

    /**
     * 初始化评估上下文
     *
     * @param rootObject 根对象
     */
    protected void initContext(Object rootObject) {
        StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
        context.setBeanResolver(beanResolver);
        if (this.globalFunctions != null) {
            for (Method method : globalFunctions) {
                context.registerFunction(method.getName(), method);
            }
        }
        RulerContextHolder.setContext(context);
    }

    /**
     * 规则执行
     *
     * @param rootObject 根对象
     * @param rule       当前规则
     * @param result     执行结果
     * @return 规则执行的结果，触发规则返回true，否则返回false
     */
    protected boolean executeInternal(Object rootObject, AbstractRule rule, Result result) {
        if (rule.supports(rootObject)) {
            if (rule instanceof SimpleRule) {
                Object value = rule.getValue(rootObject);
                if (value != null) {
                    result.addReport(rule.getRuleDefinition(), value);
                    return true;
                }
            } else {
                boolean flag = rule.evaluate(rootObject);
                if (flag) {
                    Object value = rule.getValue(rootObject);
                    result.addReport(rule.getRuleDefinition(), value);
                }
                return flag;
            }
        }
        return false;
    }

    @Override
    public String getBusinessType() {
        return businessType;
    }

    @Override
    public List<RuleDefinition> getRuleDefinitions() {
        return this.rules.stream()
                .map(AbstractRule::getRuleDefinition)
                .collect(Collectors.toList());
    }

    @Override
    public AbstractRule getRule(String ruleCode) {
        return this.rules.stream()
                .filter(rule -> rule.getRuleDefinition().getRuleCode().equals(ruleCode))
                .findAny()
                .orElse(null);
    }

    @Override
    public void addRule(String ruleCode) {
        AbstractRule rule = this.ruleFactory.getRule(ruleCode);
        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.get(i).getRuleDefinition().getOrder()
                    > rule.getRuleDefinition().getOrder()) {
                this.rules.add(i, rule);
                return;
            }
        }
        this.rules.add(rule);
    }

    @Override
    public void addRule(Collection<String> ruleCodes) {
        ruleCodes.forEach(this::addRule);
    }

    @Override
    public AbstractRule removeRule(String ruleCode) {
        if (ruleCode == null) {
            return null;
        }
        for (int i = 0; i < this.rules.size(); i++) {
            AbstractRule rule = this.rules.get(i);
            if (rule.getRuleDefinition().getRuleCode().equals(ruleCode)) {
                if (rule.getRuleDefinition().isRequired()) {
                    throw new RuntimeException("The rule [" + ruleCode + "] is required.");
                }
                return this.rules.remove(i);
            }
        }
        return null;
    }

    @Override
    public void forceRemoveRule(String ruleCode) {
        this.rules.removeIf(rule -> rule.getRuleDefinition().getRuleCode().equals(ruleCode));
    }

    @Override
    public void reloadRules() {
        List<AbstractRule> rules = ruleFactory.findRules(businessType).stream()
                .filter(rule -> rule.getRuleDefinition().isEnabled())
                .sorted(Comparator.comparingInt(rule -> rule.getRuleDefinition().getOrder()))
                .collect(Collectors.toList());
        this.rules.clear();
        this.rules.addAll(rules);
    }
}
