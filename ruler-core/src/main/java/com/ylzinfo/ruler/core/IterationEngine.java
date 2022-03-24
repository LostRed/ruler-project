package com.ylzinfo.ruler.core;

/**
 * 规则迭代引擎接口
 *
 * @author dengluwei
 */
public interface IterationEngine {
    /**
     * 是否迭代下一个规则
     *
     * @param grade 当前结果等级
     * @return 是返回true，否则返回false
     */
    boolean toNext(String grade);
}