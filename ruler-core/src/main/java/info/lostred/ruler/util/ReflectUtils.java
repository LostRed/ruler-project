package info.lostred.ruler.util;

import info.lostred.ruler.domain.NodeInfo;

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
     * @param nodeInfo 节点信息
     * @param assigned 指定类的类对象
     * @return 节点信息
     * @throws IllegalAccessException 无法获取字段的值
     * @throws NoSuchFieldException   无法在类中找到字段
     */
    public static NodeInfo findNodeByType(NodeInfo nodeInfo, Class<?> assigned) throws IllegalAccessException, NoSuchFieldException {
        return findNodeByType(nodeInfo, assigned, new ArrayList<>(2));
    }

    /**
     * 从对象的字段中找到一个指定类的值(节点)
     *
     * @param nodeInfo     节点信息
     * @param assigned     指定类的类对象
     * @param targetFields 目标字段集合
     * @return 指定类的值
     * @throws IllegalAccessException 无法获取字段的值
     * @throws NoSuchFieldException   无法在类中找到字段
     */
    public static NodeInfo findNodeByType(NodeInfo nodeInfo, Class<?> assigned, List<Field> targetFields) throws IllegalAccessException, NoSuchFieldException {
        Object object = nodeInfo.getNode();
        if (nodeInfo.getNode().getClass() == assigned) {
            return nodeInfo;
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
                        nodeInfo.setNode(field.get(object));
                        return findNodeByType(nodeInfo, assigned, targetFields);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
            if (targetFields.isEmpty()) {
                throw new NoSuchFieldException("Cannot find the assigned class in this object.");
            } else if (targetFields.size() > 1) {
                throw new RuntimeException("Find 2 or more assigned class in this object.");
            } else {
                Field field = targetFields.get(0);
                nodeInfo.setNode(field.get(object));
                nodeInfo.getTracer().append(field.getName()).append(".");
                return nodeInfo;
            }
        }
    }

    /**
     * 从对象中找到指定类的字段并获取其值(节点)
     *
     * @param object   对象
     * @param assigned 指定类的类对象
     * @return 节点信息
     */
    public static NodeInfo searchAndGetNodeByType(Object object, Class<?> assigned) {
        try {
            NodeInfo nodeInfo = new NodeInfo(object);
            return ReflectUtils.findNodeByType(nodeInfo, assigned);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            throw new RuntimeException("Failed to find valid node " + assigned.getName() + " from " + object.getClass().getName() + ".");
        }
    }

    /**
     * 集合字段中是否有包含指定类的字段
     *
     * @param nodeInfo 集合的节点信息
     * @param node     节点
     * @return 集合元素的节点信息
     */
    public static NodeInfo getNodeInfoFromCollection(NodeInfo nodeInfo, Object node) {
        String s = nodeInfo.getTrace() + Integer.toHexString(node.hashCode()) + ".";
        return new NodeInfo(node, new StringBuilder(s));
    }

    /**
     * 拼接节点链路
     *
     * @param nodeTrace 集合节点的链路
     * @param node      集合元素节点
     * @param fieldName 集合元素的某个字段名
     * @return 节点链路
     */
    public static String concatNodeTrace(String nodeTrace, Object node, String fieldName) {
        return nodeTrace + "." + Integer.toHexString(node.hashCode()) + "." + fieldName;
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
     * 从对象(包括其父类)中找到指定字段名的字段并获取其值
     *
     * @param nodeInfo  节点信息
     * @param fieldName 字段名
     * @return 节点信息
     */
    public static NodeInfo searchAndGetValueByName(NodeInfo nodeInfo, String fieldName) {
        Object object = nodeInfo.getNode();
        try {
            Field field = findFieldByName(object.getClass(), fieldName);
            field.setAccessible(true);
            nodeInfo.setNode(field.get(object));
            nodeInfo.getTracer().append(fieldName);
            return nodeInfo;
        } catch (NoSuchFieldException ignored) {
            throw new RuntimeException("Failed to find field '" + fieldName + "' from " + object.getClass().getName() + ".");
        } catch (IllegalAccessException ignored) {
            throw new RuntimeException("Failed to get value of '" + fieldName + "' from " + object.getClass().getName() + ".");
        }
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
}
