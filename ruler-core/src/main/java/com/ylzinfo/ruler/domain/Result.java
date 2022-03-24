package com.ylzinfo.ruler.domain;

import com.ylzinfo.ruler.constants.ValidGrade;

import java.util.ArrayList;
import java.util.List;

/**
 * 引擎执行的结果
 *
 * @author dengluwei
 */
public class Result {
    /**
     * 校验结果等级
     */
    private String grade;
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
    private List<Report> reports;

    private Result() {
    }

    /**
     * 构建一个默认的校验结果，该结果的校验结果等级为合格
     *
     * @return 校验结果
     * @see ValidGrade
     */
    public static Result of() {
        Result result = new Result();
        result.grade = ValidGrade.QUALIFIED.getText();
        result.reports = new ArrayList<>();
        return result;
    }

    /**
     * 添加一份报告
     *
     * @param report 报告
     */
    public void addReport(Report report) {
        this.reports.add(report);
    }

    /**
     * 更新校验结果，“违规”的优先级最高，其次是“可疑”，最后是“合格”
     *
     * @param report 报告
     * @see ValidGrade
     */
    public void updateGrade(Report report) {
        if (report != null) {
            String grade = report.getRuleInfo().getGrade();
            if (!ValidGrade.ILLEGAL.getText().equals(this.grade)
                    && !this.grade.equals(grade)) {
                this.grade = grade;
            }
        }
    }

    /**
     * 统计可疑与违规字段数量
     *
     * @return 结果
     */
    public Result statistic() {
        this.suspectedFieldCount = reports.stream()
                .filter(e -> ValidGrade.SUSPECTED.getText().equals(e.getRuleInfo().getGrade()))
                .mapToLong(e -> e.getIllegals().size())
                .sum();
        this.illegalFieldCount = reports.stream()
                .filter(e -> ValidGrade.ILLEGAL.getText().equals(e.getRuleInfo().getGrade()))
                .mapToLong(e -> e.getIllegals().size())
                .sum();
        return this;
    }

    public String getGrade() {
        return grade;
    }

    public long getSuspectedFieldCount() {
        return suspectedFieldCount;
    }

    public long getIllegalFieldCount() {
        return illegalFieldCount;
    }

    public List<Report> getReports() {
        return reports;
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
