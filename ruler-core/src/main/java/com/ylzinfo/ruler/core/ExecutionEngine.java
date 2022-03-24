package com.ylzinfo.ruler.core;

import com.ylzinfo.ruler.domain.Report;

import java.util.Optional;

/**
 * 规则执行引擎接口
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public interface ExecutionEngine<E> {
    /**
     * 执行规则，检查是否违规
     *
     * @param element 规则约束的对象
     * @param rule    规则
     * @return 违规返回true，否则返回false
     */
    boolean ruleJudge(E element, Rule<E> rule);

    /**
     * 执行规则，生成报告
     *
     * @param element 规则约束的对象
     * @param rule    规则
     * @return 报告
     */
    Optional<Report> ruleReport(E element, Rule<E> rule);
}
