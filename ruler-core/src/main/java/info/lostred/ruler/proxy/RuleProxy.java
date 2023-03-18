package info.lostred.ruler.proxy;

import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.rule.AbstractRule;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
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
     * @return 代理实例对象
     */
    public AbstractRule newProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setClassLoader(target.getClass().getClassLoader());
        enhancer.setCallback(this);
        return (AbstractRule) enhancer.create();
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
    protected void printLog(Method method, Object result) {
        String methodName = method.getName();
        RuleDefinition ruleDefinition = target.getRuleDefinition();
        if ("getInitValue".equals(methodName)) {
            logger.config("[" + ruleDefinition.getRuleCode() + " " + ruleDefinition.getGrade() + "]" +
                    "initValue=" + result + ", description=" + ruleDefinition.getDescription());
        }
    }
}
