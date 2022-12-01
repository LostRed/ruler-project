package info.lostred.ruler.test.controller;

import info.lostred.ruler.domain.Result;
import info.lostred.ruler.engine.RulesEngine;
import info.lostred.ruler.test.domain.Person;
import org.springframework.expression.EvaluationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RulesEngine rulesEngine;

    public TestController(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }

    @PostMapping("/execute")
    public Result execute(@RequestBody Person person) {
        EvaluationContext context = rulesEngine.createEvaluationContext(person);
        rulesEngine.execute(context);
        return rulesEngine.getResult(context);
    }
}
