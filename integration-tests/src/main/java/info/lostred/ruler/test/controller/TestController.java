package info.lostred.ruler.test.controller;

import info.lostred.ruler.core.RulesEngine;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.test.entity.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RulesEngine<Person> rulesEngine;

    public TestController(RulesEngine<Person> rulesEngine) {
        this.rulesEngine = rulesEngine;
    }

    @PostMapping("/execute")
    public Result execute(@RequestBody Person person) {
        return rulesEngine.execute(person);
    }

    @PostMapping("/check")
    public boolean check(@RequestBody Person person) {
        return rulesEngine.check(person);
    }
}
