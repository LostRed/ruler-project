package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.constants.ValidGrade;
import com.ylzinfo.ruler.core.AbstractRule;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.factory.RuleFactory;

import java.util.Collection;

/**
 * 简单执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class SimpleRulesEngine<E> extends RulesEngine<E> {

    public SimpleRulesEngine(RuleFactory ruleFactory, String businessType, Collection<AbstractRule<E>> abstractRules) {
        super(ruleFactory, businessType, abstractRules);
    }

    /**
     * 执行规则，生成是否违规的结果
     *
     * @param element 规则约束的对象
     * @return 违规返回true，否则返回false
     */
    public boolean execute(E element) {
        for (AbstractRule<E> abstractRule : this.abstractRules) {
            return this.ruleJudge(element, abstractRule);
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
        for (AbstractRule<E> abstractRule : this.abstractRules) {
            if (abstractRule.isSupported(element) && ValidGrade.SUSPECTED.getText().equals(abstractRule.getRuleInfo().getGrade())) {
                return this.ruleJudge(element, abstractRule);
            }
        }
        return false;
    }
}
