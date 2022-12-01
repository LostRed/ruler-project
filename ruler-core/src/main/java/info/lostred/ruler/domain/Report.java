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
     * 记录初始值
     */
    private final Object initValue;

    public static Report newInstance(String description, Object initValue) {
        return new Report(description, initValue);
    }

    private Report(String description, Object initValue) {
        this.description = description;
        this.initValue = initValue;
    }

    public String getDescription() {
        return description;
    }

    public Object getInitValue() {
        return initValue;
    }
}
