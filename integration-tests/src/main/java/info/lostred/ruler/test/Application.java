package info.lostred.ruler.test;

import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.test.rule.NameRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@RuleScan("info.lostred.ruler.test.elem")
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        NameRule bean = run.getBean(NameRule.class);
        System.out.println(bean);
    }
}
