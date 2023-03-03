package info.lostred.ruler.domain;

import java.io.Serializable;

/**
 * 规则执行报告
 *
 * @author lostred
 */
public class Report implements Serializable {
    /**
     * 规则描述
     */
    private final String description;
    /**
     * 返回值
     */
    private final Object returnValue;

    public static Report newInstance(String description, Object returnValue) {
        return new Report(description, returnValue);
    }

    private Report(String description, Object returnValue) {
        this.description = description;
        this.returnValue = returnValue;
    }

    public String getDescription() {
        return description;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
