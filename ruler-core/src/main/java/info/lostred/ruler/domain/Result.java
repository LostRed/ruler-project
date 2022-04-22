package info.lostred.ruler.domain;

import info.lostred.ruler.constant.Grade;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 引擎执行的结果
 *
 * @author lostred
 */
public class Result implements Serializable {
    /**
     * 校验结果等级
     */
    private Grade grade;
    /**
     * 可疑字段数量
     */
    private long suspectedFieldCount;
    /**
     * 违规字段数量
     */
    private long illegalFieldCount;
    /**
     * 校验报告集合
     */
    private Map<String, Report> reports;

    private Result() {
    }

    /**
     * 构建一个默认的校验结果，该结果的校验结果等级为合格
     *
     * @return 校验结果
     * @see Grade
     */
    public static Result of() {
        Result result = new Result();
        result.grade = Grade.QUALIFIED;
        result.reports = new HashMap<>();
        return result;
    }

    /**
     * 添加一份报告
     *
     * @param report 报告
     */
    public void addReport(Report report) {
        String ruleCode = report.getRuleDefinition().getRuleCode();
        Report existed = this.reports.putIfAbsent(ruleCode, report);
        if (existed != null) {
            existed.putError(report.getErrors());
        }
        this.updateGrade(report.getRuleDefinition().getGrade());
    }

    /**
     * 更新校验结果的严重等级，“违规”的优先级最高，其次是“可疑”，最后是“合格”
     *
     * @param grade 严重等级
     * @see Grade
     */
    public void updateGrade(Grade grade) {
        if (Grade.ILLEGAL.equals(grade)) {
            this.grade = Grade.ILLEGAL;
        } else if (Grade.SUSPECTED.equals(grade)) {
            this.grade = Grade.SUSPECTED;
        }
    }

    /**
     * 统计可疑与违规字段数量
     */
    public void statistic() {
        this.suspectedFieldCount = reports.values().stream()
                .filter(e -> Grade.SUSPECTED.equals(e.getRuleDefinition().getGrade()))
                .mapToLong(e -> e.getErrors().size())
                .sum();
        this.illegalFieldCount = reports.values().stream()
                .filter(e -> Grade.ILLEGAL.equals(e.getRuleDefinition().getGrade()))
                .mapToLong(e -> e.getErrors().size())
                .sum();
    }

    public Grade getGrade() {
        return grade;
    }

    public long getSuspectedFieldCount() {
        return suspectedFieldCount;
    }

    public long getIllegalFieldCount() {
        return illegalFieldCount;
    }

    public Map<String, Report> getReports() {
        return Collections.unmodifiableMap(this.reports);
    }

    @Override
    public String toString() {
        return "Result{" +
                "grade='" + grade + '\'' +
                ", suspectedCount=" + suspectedFieldCount +
                ", illegalCount=" + illegalFieldCount +
                ", reports=" + reports +
                '}';
    }
}
