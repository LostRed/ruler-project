package com.ylzinfo.ruler.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ValidClass {
    private String id;
    private String string;
    private BigDecimal number;
    private LocalDateTime time;
    private List<String> stringList;
    private List<SubValidClass> subValidClasses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<SubValidClass> getSubValidClasses() {
        return subValidClasses;
    }

    public void setSubValidClasses(List<SubValidClass> subValidClasses) {
        this.subValidClasses = subValidClasses;
    }
}
