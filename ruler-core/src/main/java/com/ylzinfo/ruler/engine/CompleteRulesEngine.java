package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.core.Rule;

import java.util.Collection;

/**
 * 完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public class CompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public CompleteRulesEngine(Collection<Rule<E>> rules) {
        super(rules);
    }

    @Override
    public boolean toNext(String grade) {
        return true;
    }
}
