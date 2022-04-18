package info.lostred.ruler.core;

import info.lostred.ruler.constants.ValidGrade;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.rule.SingleFieldRule;
import net.sf.cglib.proxy.Enhancer;
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
public class AbstractRuleProxy implements MethodInterceptor {
    /**
     * 代理目标对象
     */
    private final AbstractRule<?> target;
    /**
     * 日志
     */
    private final Logger logger;

    public AbstractRuleProxy(AbstractRule<?> abstractRule) {
        this.target = abstractRule;
        this.logger = Logger.getLogger(target.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        if (target instanceof SingleFieldRule) {
            return (T) enhancer.create(new Class[]{RuleInfo.class, ValidConfiguration.class}, new Object[]{null, null});
        } else {
            return (T) enhancer.create(new Class[]{RuleInfo.class}, new Object[]{null});
        }
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = methodProxy.invoke(target, args);
        if ("buildReport".equals(method.getName()) && result instanceof Report) {
            Report report = (Report) result;
            Map<String, Object> illegals = report.getIllegals();
            if (illegals == null || illegals.isEmpty()) {
                logger.config("grade=" + ValidGrade.QUALIFIED.name() + ", report=" + illegals);
            } else {
                logger.config("grade=" + report.getRuleInfo().getGrade() + ", report=" + illegals);
            }
        } else if ("judge".equals(method.getName()) && result instanceof Boolean) {
            if ((Boolean) result) {
                logger.config("grade=" + target.getRuleInfo().getGrade());
            } else {
                logger.config("grade=" + ValidGrade.QUALIFIED.name());
            }
        }
        return result;
    }
}
