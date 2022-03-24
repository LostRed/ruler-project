package com.ylzinfo.ruler.engine;

import com.ylzinfo.ruler.core.IterationEngine;
import com.ylzinfo.ruler.core.Rule;
import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.Result;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * 详细执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
public abstract class DetailRulesEngine<E> extends RulesEngine<E> implements IterationEngine {

    public DetailRulesEngine(Collection<Rule<E>> rules) {
        super(rules);
    }

    /**
     * 执行规则，生成结果
     *
     * @param element 规则约束的对象
     * @return 结果
     */
    public Result execute(E element) {
        this.checkBeforeExecute(element);
        Result result = Result.of();
        Iterator<Rule<E>> iterator = this.rules.iterator();
        while (iterator.hasNext() && this.toNext(result.getGrade())) {
            Rule<E> rule = iterator.next();
            Optional<Report> report = this.ruleReport(element, rule);
            report.ifPresent(result::addReport);
            result.updateGrade(report.orElse(null));
        }
        return result.statistic();
    }
}
