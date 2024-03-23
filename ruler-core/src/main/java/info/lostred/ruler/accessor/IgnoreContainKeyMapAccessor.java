package info.lostred.ruler.accessor;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * EL属性访问器，允许EL能够像访问对象一样访问{@link java.util.Map}的key，如果key不存在时，返回null
 */
public class IgnoreContainKeyMapAccessor extends MapAccessor {
    @Override
    public boolean canRead(EvaluationContext context, @Nullable Object target, String name) {
        return target instanceof Map;
    }


    @Override
    public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
        Assert.state(target instanceof Map, "Target must be of type Map");
        Map<?, ?> map = (Map<?, ?>) target;
        Object value = map.get(name);
        return new TypedValue(value);
    }
}
