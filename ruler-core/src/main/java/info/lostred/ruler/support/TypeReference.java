package info.lostred.ruler.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型引用
 *
 * @param <T> 泛型类型
 * @author lostred
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
    /**
     * 暂存的泛型类型
     */
    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
