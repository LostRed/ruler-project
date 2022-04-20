package info.lostred.ruler.engine;

import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.core.Reportable;
import info.lostred.ruler.domain.Report;

/**
 * 规则执行引擎接口
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public interface ExecutionEngine<E> {
    /**
     * 执行规则的judge方法
     *
     * @param element   参数
     * @param judgement 判断器接口
     * @return 违规返回true，否则返回false
     */
    boolean doJudge(E element, Judgement<E> judgement);

    /**
     * 执行规则，生成报告
     *
     * @param element    参数
     * @param reportable 可生成报告接口
     * @return 报告
     */
    Report doBuildReport(E element, Reportable<E> reportable);
}
