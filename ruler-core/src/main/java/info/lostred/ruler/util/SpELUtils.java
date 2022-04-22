package info.lostred.ruler.util;

public final class SpELUtils {
    public static String dictLabel(String dictType) {
        String dictMap = beanLabel("dictMap");
        return "@" + dictMap + ".get('" + dictType + "')";
    }

    public static String beanLabel(String beanName) {
        return "@" + beanName;
    }
}
