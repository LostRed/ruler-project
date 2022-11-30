package info.lostred.ruler.rule;

import info.lostred.ruler.domain.RuleDefinition;

/**
 * 声明式规则
 * <p>需要定义parameterExp参数表达式，conditionExp条件表达式和predicateExp断定表达式，规则引擎根据规则的这三个表达式执行判断。</p>
 *
 * @author lostred
 */
public class DeclarativeRule extends AbstractRule {
    public DeclarativeRule(RuleDefinition ruleDefinition) {
        super(ruleDefinition);
    }
}
