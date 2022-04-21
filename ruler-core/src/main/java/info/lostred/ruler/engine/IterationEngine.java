package info.lostred.ruler.engine;

import info.lostred.ruler.constant.Grade;

/**
 * 规则迭代引擎接口
 *
 * @author lostred
 */
public interface IterationEngine {
    /**
     * 是否迭代下一个规则
     *
     * @param grade 当前严重等级
     * @return 是返回true，否则返回false
     */
    boolean toNext(Grade grade);
}
