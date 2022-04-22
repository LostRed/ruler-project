package info.lostred.ruler.proxy;

import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.rule.AbstractRule;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 抽象规则代理
 *
 * @author lostred
 */
public abstract class AbstractRuleProxy implements MethodInterceptor {
    /**
     * 代理目标对象
     */
    protected final AbstractRule target;
    /**
     * 日志
     */
    private final Logger logger;

    public AbstractRuleProxy(AbstractRule abstractRule) {
        this.target = abstractRule;
        this.logger = Logger.getLogger(target.getClass().getName());
    }

    /**
     * 打印日志
     *
     * @param method 方法
     * @param result 方法返回值
     */
    protected void printLog(Method method, Object result) {
        if ("collectMappings".equals(method.getName()) && result instanceof Report) {
            Report report = (Report) result;
            Map<String, Object> illegals = report.getErrors();
            if (illegals == null || illegals.isEmpty()) {
                logger.config("ruleCode=" + target.getRuleDefinition().getRuleCode() +
                        ", grade=" + Grade.QUALIFIED.name() +
                        ", report=" + illegals);
            } else {
                logger.config("ruleCode=" + target.getRuleDefinition().getRuleCode() +
                        ", grade=" + report.getRuleDefinition().getGrade() +
                        ", report=" + illegals);
            }
        } else if ("judge".equals(method.getName()) && result instanceof Boolean) {
            if ((Boolean) result) {
                logger.config("ruleCode=" + target.getRuleDefinition().getRuleCode() +
                        ", grade=" + target.getRuleDefinition().getGrade());
            } else {
                logger.config("ruleCode=" + target.getRuleDefinition().getRuleCode() +
                        ", grade=" + Grade.QUALIFIED.name());
            }
        }
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = methodProxy.invoke(target, args);
        this.printLog(method, result);
        return result;
    }
}
