package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.constants.ValidGrade;

import java.util.Collection;

/**
 * 简单执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class SimpleRulesEngine<E> extends RulesEngine<E> {

    public SimpleRulesEngine(Collection<Rule<E>> rules) {
        super(rules);
    }

    /**
     * 执行规则，生成是否违规的结果
     *
     * @param element 规则约束的对象
     * @return 违规返回true，否则返回false
     */
    public boolean execute(E element) {
        for (Rule<E> rule : this.rules) {
            return this.ruleJudge(element, rule);
        }
        return false;
    }

    /**
     * 检查是否有可疑的报告
     *
     * @param element 规则约束的对象
     * @return 有返回true，否则返回false
     */
    public boolean checkSuspicious(E element) {
        for (Rule<E> rule : this.rules) {
            if (rule.isSupported(element) && ValidGrade.SUSPECTED.getText().equals(rule.getRuleInfo().getGrade())) {
                return this.ruleJudge(element, rule);
            }
        }
        return false;
    }
}
