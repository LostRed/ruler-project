package info.lostred.ruler.rule;

import info.lostred.ruler.core.Evaluator;
import info.lostred.ruler.domain.RuleDefinition;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Evaluator {
    /**
     * 规则定义
     */
    private RuleDefinition ruleDefinition;
    /**
     * 表达式解析器
     */
    private ExpressionParser expressionParser;
    /**
     * bean解析器
     */
    private BeanResolver beanResolver;

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public void setRuleDefinition(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public BeanResolver getBeanResolver() {
        return beanResolver;
    }

    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     * 从bean解析器中解析bean
     *
     * @param beanName  bean的名称
     * @param beanClass bean的类对象
     * @param <T>       bean类型
     * @return bean对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T getBean(String beanName, Class<T> beanClass) {
        try {
            Object object = this.getBeanResolver().resolve(null, beanName);
            if (beanClass.isAssignableFrom(object.getClass())) {
                return (T) object;
            }
            throw new RuntimeException("no qualifying bean of type '" + beanClass.getName() + "' available");
        } catch (AccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 规则初始化
     */
    public void init() {
    }

    /**
     * 在给定的评估上下文与表达式解析器下，评估接口是否支持对该参数进行判断
     *
     * @param object 参数
     * @return 支持返回true，否则返回false
     */
    public abstract boolean supports(Object object);

    /**
     * 获取需要记录的值
     *
     * @param object 参数
     * @return 需要记录的值
     */
    public abstract Object getValue(Object object);
}
