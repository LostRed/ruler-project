package info.lostred.ruler.domain;

import java.util.*;

/**
 * 规则执行报告
 *
 * @author lostred
 */
public class Report {
    /**
     * 规则信息
     */
    private final RuleDefinition ruleDefinition;
    /**
     * 违规字段与值的映射
     */
    private final Map<String, Object> errors;

    public static Report of(RuleDefinition ruleDefinition) {
        return new Report(ruleDefinition);
    }

    private Report(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
        this.errors = new HashMap<>();
    }

    /**
     * 添加违规字段与值的映射
     *
     * @param fieldName 字段名
     * @param value     值
     * @return 返回调用对象
     */
    public Report putError(String fieldName, Object value) {
        this.errors.put(fieldName, value);
        return this;
    }

    /**
     * 添加违规字段与值的映射的集合
     *
     * @param entries 映射集合
     * @return 返回调用对象
     */
    public Report putError(Set<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            this.errors.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 添加违规字段与值的映射集合
     *
     * @param map 字段与值的键值对集合
     * @return 返回调用对象
     */
    public Report putError(Map<String, Object> map) {
        this.errors.putAll(map);
        return this;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public Map<String, Object> getErrors() {
        return Collections.unmodifiableMap(this.errors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Report report = (Report) o;
        return Objects.equals(ruleDefinition, report.ruleDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleDefinition);
    }

    @Override
    public String toString() {
        return "Report{" +
                "ruleDefinition=" + ruleDefinition +
                ", errors=" + errors +
                '}';
    }
}
