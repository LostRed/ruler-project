package info.lostred.ruler.engine;

import info.lostred.ruler.factory.RuleFactory;

/**
 * 完整执行的规则引擎
 *
 * @param <E> 规则约束的参数类型
 * @author lostred
 */
public class CompleteRulesEngine<E> extends DetailRulesEngine<E> {

    public CompleteRulesEngine(RuleFactory ruleFactory, String businessType) {
        super(ruleFactory, businessType);
    }

    @Override
    public boolean toNext(String grade) {
        return true;
    }
}
