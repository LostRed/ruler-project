package info.lostred.ruler.engine;

import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.Reportable;
import info.lostred.ruler.domain.Report;

/**
 * 规则执行引擎接口
 *
 * @param <T> 规则约束的参数类型
 * @author lostred
 */
public interface ExecutionEngine<T> {
    /**
     * 执行规则的judge方法
     *
     * @param object    参数
     * @param judgement 判断器接口
     * @return 违规返回true，否则返回false
     */
    boolean doJudge(T object, Judgement<T> judgement);

    /**
     * 执行规则，生成报告
     *
     * @param object     参数
     * @param reportable 可生成报告接口
     * @return 报告
     */
    Report doBuildReport(T object, Reportable<T> reportable);
}
