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

    public static Report newInstance(Grade grade, String description, Object returnValue) {
        return new Report(grade.name(), description, returnValue);
    }

    private Report(String grade, String description, Object returnValue) {
        this.grade = grade;
        this.description = description;
        this.returnValue = returnValue;
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
