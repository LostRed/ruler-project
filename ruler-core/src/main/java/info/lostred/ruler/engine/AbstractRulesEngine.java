package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static info.lostred.ruler.constant.RulerConstants.RESULT;

/**
 * 抽象规则引擎
 *
 * @author lostred
 */
public abstract class AbstractRulesEngine implements RulesEngine {
    /**
     * 规则工厂
     */
    private final RuleFactory ruleFactory;
    /**
     * 业务类型
     */
    private final String businessType;
    /**
     * bean解析器
     */
    protected final BeanResolver beanResolver;
    /**
     * 表达式解析器
     */
    protected final ExpressionParser parser;
    /**
     * 全局函数
     */
    protected final List<Method> globalFunctions;
    /**
     * 规则引擎中的规则集合
     */
    protected final List<AbstractRule> rules = new CopyOnWriteArrayList<>();

    public AbstractRulesEngine(RuleFactory ruleFactory, String businessType,
                               BeanResolver beanResolver, ExpressionParser parser, List<Method> globalFunctions) {
        this.ruleFactory = ruleFactory;
        this.businessType = businessType;
        this.beanResolver = beanResolver;
        this.parser = parser;
        this.globalFunctions = globalFunctions;
        this.reloadRules();
    }

    @Override
    public EvaluationContext createEvaluationContext(Object input) {
        StandardEvaluationContext context = new StandardEvaluationContext(input);
        context.setVariable(RESULT, Result.newInstance());
        context.setBeanResolver(beanResolver);
        if (this.globalFunctions != null) {
            for (Method method : globalFunctions) {
                context.registerFunction(method.getName(), method);
            }
        }
        return context;
    }

    @Override
    public Result getResult(EvaluationContext context) {
        return this.parser.parseExpression("#" + RESULT).getValue(context, Result.class);
    }

    /**
     * 针对数组参数执行
     *
     * @param context 评估上下文
     * @param rule    规则
     * @param array   数组参数
     * @return 结果，数组中的所有元素有一个不通过时返回true，否则返回false
     */
    protected boolean executeForArray(EvaluationContext context, AbstractRule rule, Object[] array) {
        if (array != null) {
            boolean flag = false;
            for (int i = 0; i < array.length; i++) {
                flag = flag || this.executeForObject(context, rule);
            }
            return flag;
        }
        return false;
    }

    /**
     * 针对对象参数执行
     *
     * @param context 评估上下文
     * @param rule    规则
     * @return 结果
     */
    protected boolean executeForObject(EvaluationContext context, AbstractRule rule) {
        if (rule.supports(context, parser)) {
            boolean flag = rule.evaluate(context, parser);
            if (flag) {
                Object value = rule.getValue(context, parser);
                this.getResult(context).addInitValue(rule.getRuleDefinition(), value);
            }
            return flag;
        }
        return false;
    }

    /**
     * 规则执行
     *
     * @param context 评估上下文
     * @param rule    当前规则
     * @return 规则执行的结果，触发规则返回true，否则返回false
     */
    protected boolean executeInternal(EvaluationContext context, AbstractRule rule) {
        String parameterExp = rule.getRuleDefinition().getParameterExp();
        Expression expression = parser.parseExpression(parameterExp);
        Class<?> valueType = expression.getValueType(context);
        assert valueType != null;
        if (Collection.class.isAssignableFrom(valueType) || Object[].class.isAssignableFrom(valueType)) {
            Object[] array = parser.parseExpression(parameterExp).getValue(context, Object[].class);
            return this.executeForArray(context, rule, array);
        } else {
            return this.executeForObject(context, rule);
        }
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
