package com.ylzinfo.ruler.constants;

/**
 * 校验结果等级
 *
 * @author dengluwei
 */
public enum ValidGrade {
    QUALIFIED("合格"),
    SUSPECTED("可疑"),
    ILLEGAL("违规")
    ;

    private final String text;

    ValidGrade(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
