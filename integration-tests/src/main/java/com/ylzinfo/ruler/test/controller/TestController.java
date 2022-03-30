package com.ylzinfo.ruler.test.controller;

import com.ylzinfo.ruler.core.RulesEngine;
import com.ylzinfo.ruler.domain.Result;
import com.ylzinfo.ruler.test.entity.Person;
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
