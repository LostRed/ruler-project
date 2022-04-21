package info.lostred.ruler.test.config;

import info.lostred.ruler.annotation.RuleScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RuleScan("info.lostred.ruler.test.rule")
public class RulerConfig {
    private static final String businessType = "person";
}
