package info.lostred.ruler.rule;

import info.lostred.ruler.core.Collector;
import info.lostred.ruler.core.Judgement;
import info.lostred.ruler.domain.RuleDefinition;

/**
 * 抽象规则
 *
 * @author lostred
 */
public abstract class AbstractRule implements Judgement, Collector {
    /**
     * 规则定义
     */
    protected final RuleDefinition ruleDefinition;

    public AbstractRule(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
