package info.lostred.ruler.test.domain;

import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.test.domain.model.ValidClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RuleInfoTest {
    @Test
    void should_equals_when_two_rule_infos_have_the_same_rule_code() {
        RuleInfo ruleInfo1 = RuleInfo.of("testCode", "test1", "违规", "A test rule.",
                0, true, true, "test", ValidClass.class);
        RuleInfo ruleInfo2 = RuleInfo.of("testCode", "test2", "警告", "A test rule.",
                0, true, true, "test", ValidClass.class);
        Assertions.assertEquals(ruleInfo2, ruleInfo1);
    }
}
