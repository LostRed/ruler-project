package info.lostred.ruler.test.config;

import info.lostred.ruler.annotation.RuleScan;
import info.lostred.ruler.util.DateTimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
@RuleScan("info.lostred.ruler.test.rule")
public class RulerConfig {
    @Bean
    public Map<String, Set<String>> dict() {
        Map<String, Set<String>> dict = new HashMap<>();
        Set<String> set = new HashSet<>(Arrays.asList("1", "2"));
        dict.put("gender", set);
        return dict;
    }

    @Bean
    public List<Method> globalFunctions() {
        return Arrays.asList(DateTimeUtils.class.getMethods());
    }
}
