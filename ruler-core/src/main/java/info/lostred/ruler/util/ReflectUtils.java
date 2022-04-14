package info.lostred.ruler.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 反射工具
 *
 * @author lostred
 */
public final class ReflectUtils {
    /**
     * 从对象的字段中找到一个指定类的值(节点)
     *
     * @param object   对象
     * @param assigned 指定类的类对象
     * @return 指定类的值
     * @throws IllegalAccessException 无法获取字段的值
     * @throws NoSuchFieldException   无法在类中找到字段
     */
    public static Object findNodeByType(Object object, Class<?> assigned) throws IllegalAccessException, NoSuchFieldException {
        return findNodeByType(object, assigned, new ArrayList<>(2));
    }

    /**
     * 从对象的字段中找到一个指定类的值(节点)
     *
     * @param object       对象
     * @param assigned     指定类的类对象
     * @param targetFields 目标字段集合
     * @return 指定类的值
     * @throws IllegalAccessException 无法获取字段的值
     * @throws NoSuchFieldException   无法在类中找到字段
     */
    public static Object findNodeByType(Object object, Class<?> assigned, List<Field> targetFields) throws IllegalAccessException, NoSuchFieldException {
        if (object.getClass() == assigned) {
            return object;
        } else {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == assigned) {
                    targetFields.add(field);
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    if (include(field, assigned)) {
                        targetFields.add(field);
                    }
                } else if (isEntity(field.getType())) {
                    try {
                        return findNodeByType(field.get(object), assigned, targetFields);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
            if (targetFields.isEmpty()) {
                throw new NoSuchFieldException("Cannot find the assigned class in this object.");
            } else if (targetFields.size() > 1) {
                throw new RuntimeException("Find 2 or more assigned class in this object.");
            } else {
                return targetFields.get(0).get(object);
            }
        }
    }

    /**
     * 从对象中找到指定类的字段并获取其值(节点)
     *
     * @param object   对象
     * @param assigned 指定类的类对象
     * @return 字段的值
     */
    public static Object searchAndGetNodeByType(Object object, Class<?> assigned) {
        try {
            return ReflectUtils.findNodeByType(object, assigned);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            throw new RuntimeException("Failed to find valid node " + assigned.getName() + " from " + object.getClass().getName() + ".");
        }
    }

    /**
     * 集合字段中是否有包含指定类的字段
     *
     * @param collectionField 集合字段
     * @param assignedClass   指定类的类对象
     * @return 有返回true，否则返回false
     */
    public static boolean include(Field collectionField, Class<?> assignedClass) {
        Type type = collectionField.getGenericType();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                Type actualTypeArgument = actualTypeArguments[0];
                return actualTypeArgument.getTypeName().equals(assignedClass.getTypeName());
            }
        }
        return false;
    }

    /**
     * 是否是实体类类型
     *
     * @param _class 类的类对象
     * @return 是返回true，否则返回false
     */
    public static boolean isEntity(Class<?> _class) {
        return !String.class.equals(_class)
                && !Temporal.class.isAssignableFrom(_class)
                && !Date.class.isAssignableFrom(_class)
                && !Number.class.isAssignableFrom(_class);
    }

    /**
     * 从对象(包括其父类)中找到指定字段名的字段
     *
     * @param child     子类
     * @param fieldName 字段名
     * @return 字段
     * @throws NoSuchFieldException 无法在类中找到字段
     */
    public static Field findFieldByName(Class<?> child, String fieldName) throws NoSuchFieldException {
        try {
            return child.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
            Class<?> parent = child.getSuperclass();
            return findFieldByName(parent, fieldName);
        } catch (NullPointerException ignored) {
            throw new NoSuchFieldException("Cannot find this fieldName.");
        }
    }

    /**
     * 从对象(包括其父类)中找到指定字段名的字段并获取其值
     *
     * @param object    对象
     * @param fieldName 字段名
     * @return 字段的值
     */
    public static Object searchAndGetValueByName(Object object, String fieldName) {
        try {
            Field field = findFieldByName(object.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException ignored) {
            throw new RuntimeException("Failed to find field '" + fieldName + "' from " + object.getClass().getName() + ".");
        } catch (IllegalAccessException ignored) {
            throw new RuntimeException("Failed to get value of '" + fieldName + "' from " + object.getClass().getName() + ".");
        }
    }

    /**
     * 从当前类中获取指定类中指定字段的链路字符串
     *
     * @param current   当前类的类对象
     * @param assigned  指定类的类对象
     * @param fieldName 指定的字段名
     * @param value     指定字段的值
     * @return 校验字段的链路
     * @throws NoSuchFieldException 无法在类中找到字段
     */
    public static String getFieldTrace(Class<?> current, Class<?> assigned,
                                       String fieldName, Object value) throws NoSuchFieldException {
        return getFieldTrace(current, assigned, fieldName, value, new StringBuilder(), new ArrayList<>(2));
    }

    /**
     * 从当前类中获取指定类中指定字段的链路字符串
     *
     * @param current       当前类的类对象
     * @param assigned      指定类的类对象
     * @param fieldName     指定的字段名
     * @param value         指定字段的值
     * @param stringBuilder 字符串拼接
     * @param targetFields  目标字段集合
     * @return 校验字段的链路
     * @throws NoSuchFieldException 无法在类中找到字段
     */
    public static String getFieldTrace(Class<?> current, Class<?> assigned,
                                       String fieldName, Object value,
                                       StringBuilder stringBuilder, List<Field> targetFields) throws NoSuchFieldException {
        if (current.equals(assigned)) {
            return fieldName;
        } else {
            Field[] fields = current.getDeclaredFields();
            for (Field field : fields) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    if (include(field, assigned)) {
                        field.setAccessible(true);
                        stringBuilder.append(field.getName())
                                .append("[").append(Integer.toHexString(value.hashCode())).append("]")
                                .append(".").append(fieldName);
                        targetFields.add(field);
                    }
                } else if (isEntity(field.getType())) {
                    stringBuilder.append(field.getName());
                    try {
                        return getFieldTrace(field.getType(), assigned,
                                fieldName, value,
                                stringBuilder.append("."), targetFields);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
            if (targetFields.isEmpty()) {
                throw new NoSuchFieldException("Cannot find the assigned class in root class.");
            } else if (targetFields.size() > 1) {
                throw new RuntimeException("Find 2 or more assigned class in root class.");
            } else {
                return stringBuilder.toString();
            }
        }
    }
}
