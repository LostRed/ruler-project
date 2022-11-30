package info.lostred.ruler.test.controller;

import info.lostred.ruler.engine.RulesEngine;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RulesEngine rulesEngine;

    public TestController(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }

//    @PostMapping("/execute")
//    public Result execute(@RequestBody Person person) {
//        return rulesEngine.execute(person);
//    }
}
