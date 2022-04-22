package info.lostred.ruler.engine;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.AbstractRule;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.lostred.ruler.constant.RulerConstants.INDEX_KEY;
import static info.lostred.ruler.constant.RulerConstants.INDEX_LABEL;

/**
 * 规则引擎
 *
 * @author lostred
 */
public abstract class RulesEngine {
    private final RuleFactory ruleFactory;
    private final String businessType;
    protected final BeanResolver beanResolver;
    protected final ExpressionParser parser;
    protected final List<AbstractRule> rules = new CopyOnWriteArrayList<>();

    public RulesEngine(RuleFactory ruleFactory, String businessType,
                       BeanResolver beanResolver, ExpressionParser parser) {
        this.ruleFactory = ruleFactory;
        this.businessType = businessType;
        this.beanResolver = beanResolver;
        this.parser = parser;
        List<AbstractRule> rules = ruleFactory.findRules(businessType);
        this.rules.addAll(rules);
    }

    /**
     * 评估结果
     *
     * @param object 待校验的对象
     * @return 引擎执行的布尔结果，true为不通过，false为通过
     */
    public boolean evaluate(Object object) {
        StandardEvaluationContext context = new StandardEvaluationContext(object);
        this.setBeanResolver(context);
        for (AbstractRule rule : rules) {
            if (this.handle(context, object, rule)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行规则
     *
     * @param object 待校验的对象
     * @return 引擎执行的结果
     */
    public abstract Result execute(Object object);

    /**
     * 针对数组参数执行
     *
     * @param context 评估上下文
     * @param array   数组参数
     * @param rule    规则
     * @return 结果，数组中的所有元素有一个不通过时返回true，否则返回false
     */
    protected boolean executeForArray(StandardEvaluationContext context, Object[] array,
                                      AbstractRule rule) {
        if (array != null) {
            boolean flag = false;
            for (int i = 0; i < array.length; i++) {
                context.setVariable(INDEX_KEY, i);
                flag = flag || this.executeForObject(context, array[i], rule);
            }
            return flag;
        }
        return false;
    }

    /**
     * 针对数组参数执行
     *
     * @param context 评估上下文
     * @param array   数组参数
     * @param rule    规则
     * @param result  引擎执行的结果
     */
    protected void executeForArray(StandardEvaluationContext context, Object[] array,
                                   AbstractRule rule, Result result) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                context.setVariable(INDEX_KEY, i);
                this.executeForObject(context, array[i], rule, result);
            }
        }
    }

    /**
     * 针对对象参数执行
     *
     * @param context 评估上下文
     * @param object  对象参数
     * @param rule    规则
     * @return 结果
     */
    protected boolean executeForObject(StandardEvaluationContext context, Object object,
                                       AbstractRule rule) {
        if (rule.isSupported(context, parser, object)) {
            return rule.judge(context, parser, object);
        }
        return false;
    }

    /**
     * 针对对象参数执行
     *
     * @param context 评估上下文
     * @param object  对象参数
     * @param rule    规则
     * @param result  引擎执行的结果
     */
    protected void executeForObject(StandardEvaluationContext context, Object object,
                                    AbstractRule rule, Result result) {
        if (rule.isSupported(context, parser, object)) {
            if (rule.judge(context, parser, object)) {
                Map<String, Object> map = rule.collectMappings(context, parser, object);
                Report report = Report.of(rule.getRuleDefinition()).putError(map);
                result.addReport(report);
            }
        }
    }

    /**
     * 设置bean解析器
     *
     * @param context 评估上下文
     */
    protected void setBeanResolver(StandardEvaluationContext context) {
        context.setBeanResolver(beanResolver);
    }

    /**
     * 设置参数
     *
     * @param context 评估上下文
     * @param name    参数名
     * @param object  参数
     */
    protected void setVariable(StandardEvaluationContext context, String name, Object object) {
        context.setVariable(name, object);
    }

    public String getBusinessType() {
        return businessType;
    }

    /**
     * 针对无详细结果的处理
     *
     * @param context 评估上下文
     * @param object  待校验的对象
     * @param rule    当前规则
     * @return 结果，true表示不通过，false表示通过
     */
    protected boolean handle(StandardEvaluationContext context, Object object, AbstractRule rule) {
        String parameterExp = rule.getRuleDefinition().getParameterExp();
        if (parameterExp.contains(INDEX_LABEL)) {
            String arrayExp = parameterExp.substring(0, parameterExp.indexOf(INDEX_LABEL));
            Object[] array = parser.parseExpression(arrayExp).getValue(context, Object[].class);
            return this.executeForArray(context, array, rule);
        } else {
            return this.executeForObject(context, object, rule);
        }
    }
}
