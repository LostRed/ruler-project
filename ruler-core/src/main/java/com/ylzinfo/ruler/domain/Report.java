package com.ylzinfo.ruler.domain;

import java.util.*;

/**
 * 规则执行报告
 *
 * @author dengluwei
 */
public class Report {
    /**
     * 规则信息
     */
    private final RuleInfo ruleInfo;
    /**
     * 非法字段与值的映射
     */
    private final Map<String, Object> illegals;

    public static Report of(RuleInfo ruleInfo) {
        return new Report(ruleInfo);
    }

    private Report(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
        this.illegals = new HashMap<>();
    }

    public Report putIllegals(String fieldName, Object value) {
        this.illegals.put(fieldName, value);
        return this;
    }

    public Report putIllegals(Set<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            this.illegals.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Report putIllegals(Map<String, Object> map) {
        this.illegals.putAll(map);
        return this;
    }

    public RuleInfo getRuleInfo() {
        try {
            return (RuleInfo) ruleInfo.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getIllegals() {
        return Collections.unmodifiableMap(this.illegals);
    }

    @Override
    public String toString() {
        return "Report{" +
                "ruleInfo=" + ruleInfo +
                ", illegals=" + illegals +
                '}';
    }
}
