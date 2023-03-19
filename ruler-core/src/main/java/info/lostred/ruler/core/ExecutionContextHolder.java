package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;

/**
 * 执行上下文
 *
 * @author lostred
 * @since 3.1.0
 */
public class ExecutionContextHolder {
    private static final ThreadLocal<EvaluationContext> CONTEXT_HOLDER = new ThreadLocal<>();

    public static EvaluationContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void setContext(EvaluationContext context) {
        CONTEXT_HOLDER.set(context);
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
