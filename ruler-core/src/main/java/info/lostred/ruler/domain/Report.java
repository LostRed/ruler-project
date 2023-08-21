package info.lostred.ruler.domain;

import info.lostred.ruler.constant.Grade;

import java.io.Serializable;

/**
 * 规则执行报告
 *
 * @author lostred
 */
public class Report implements Serializable {
    /**
     * 类型
     */
    private final String type;
    /**
     * 错误等级
     */
    private final String grade;
    /**
     * 规则描述
     */
    private final String description;
    /**
     * 返回值
     */
    private final Object returnValue;

    public static Report newInstance(String type, Grade grade, String description, Object returnValue) {
        return new Report(type, grade.name(), description, returnValue);
    }

    private Report(String type, String grade, String description, Object returnValue) {
        this.type = type;
        this.grade = grade;
        this.description = description;
        this.returnValue = returnValue;
    }

    public String getType() {
        return type;
    }

    public String getGrade() {
        return grade;
    }

    public String getDescription() {
        return description;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
