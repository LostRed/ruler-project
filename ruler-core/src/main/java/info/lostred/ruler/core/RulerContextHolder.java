package info.lostred.ruler.core;

import org.springframework.expression.EvaluationContext;

public class RulerContextHolder {
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
