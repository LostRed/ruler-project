package info.lostred.ruler.proxy;

import info.lostred.ruler.constant.Grade;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 规则代理
 *
 * @author lostred
 */
public class RuleProxy implements MethodInterceptor {
    /**
     * 代理目标对象
     */
    protected final AbstractRule target;
    /**
     * 日志
     */
    private final Logger logger;

    public RuleProxy(AbstractRule abstractRule) {
        this.target = abstractRule;
        this.logger = Logger.getLogger(target.getClass().getName());
    }

    /**
     * 创建一个代理实例对象
     *
     * @param <T> 代理目标的类型
     * @return 代理实例对象
     */
    @SuppressWarnings("unchecked")
    public <T> T newProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create(new Class[]{RuleDefinition.class}, new Object[]{null});
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = methodProxy.invoke(target, args);
        this.printLog(method, result);
        return result;
    }

    /**
     * 打印日志
     *
     * @param method 方法
     * @param result 方法返回值
     */
    @SuppressWarnings("unchecked")
    protected void printLog(Method method, Object result) {
        if ("collectMappings".equals(method.getName()) && result instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) result;
            if (!map.isEmpty()) {
                logger.config("ruleCode=" + target.getRuleDefinition().getRuleCode() +
                        ", report=" + map);
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
}
