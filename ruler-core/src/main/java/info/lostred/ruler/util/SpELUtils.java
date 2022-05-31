package info.lostred.ruler.util;

public final class SpELUtils {
    public static String dictLabel(String dictBeanName, String dictType) {
        String dictMap = beanLabel(dictBeanName);
        return dictMap + ".get('" + dictType + "')";
    }

    public static String beanLabel(String beanName) {
        return "@" + beanName;
    }
}
