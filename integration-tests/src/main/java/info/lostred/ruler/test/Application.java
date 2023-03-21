package info.lostred.ruler.test;

import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.factory.DomainFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

@SpringBootApplication
@RuleScan("info.lostred.ruler.test.elem")
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        DomainFactory bean = run.getBean(DomainFactory.class);
        Set<Class<?>> allDomain = bean.getAllDomain();
        allDomain.forEach(System.out::println);
    }
}
