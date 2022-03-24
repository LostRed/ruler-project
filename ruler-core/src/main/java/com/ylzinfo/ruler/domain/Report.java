package com.ylzinfo.ruler.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 规则执行报告
 *
 * @author dengluwei
 */
public class Report {
    /**
     * 规则信息
     */
    private RuleInfo ruleInfo;
    /**
     * 非法字段与值的映射
     */
    private Map<String, Object> illegals = new HashMap<>();

    private Report() {
    }

    public static Report of(RuleInfo ruleInfo) {
        Report report = new Report();
        report.ruleInfo = ruleInfo;
        return report;
    }

    public Report putIllegal(String fieldName, Object value) {
        this.illegals.put(fieldName, value);
        return this;
    }

    public Report putIllegal(Set<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            this.illegals.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Report putIllegal(Map<String, Object> map) {
        this.illegals.putAll(map);
        return this;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    public Map<String, Object> getIllegals() {
        return illegals;
    }

    public void setIllegals(Map<String, Object> illegals) {
        this.illegals = illegals;
    }

    @Override
    public String toString() {
        return "Report{" +
                "ruleInfo=" + ruleInfo +
                ", illegals=" + illegals +
                '}';
    }
}
