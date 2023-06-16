package info.lostred.ruler.test.controller;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleDefinition;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.factory.RuleFactory;
import info.lostred.ruler.rule.DeclarativeRule;
import info.lostred.ruler.test.domain.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final RuleFactory ruleFactory;
    private final RulesEngine rulesEngine;

    public TestController(RuleFactory ruleFactory, RulesEngine rulesEngine) {
        this.ruleFactory = ruleFactory;
        this.rulesEngine = rulesEngine;
    }

    @PostMapping("/execute")
    public Result execute(@RequestBody Person person) {
        return rulesEngine.execute(person);
    }

    @PostMapping("/addRule")
    public String addRule(@RequestBody RuleDefinition ruleDefinition) {
        if (ruleDefinition.getRuleClass() == null) {
            ruleDefinition.setRuleClass(DeclarativeRule.class);
        }
        ruleFactory.registerRuleDefinition(ruleDefinition);
        rulesEngine.addRule(ruleDefinition.getRuleCode());
        return "ok";
    }
}
