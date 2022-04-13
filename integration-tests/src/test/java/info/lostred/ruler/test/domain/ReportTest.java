package info.lostred.ruler.test.domain;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReportTest {
    @Test
    void should_get_another_rule_info() {
        RuleInfo ruleInfo = new RuleInfo();
        Report report = Report.of(ruleInfo);
        RuleInfo another = report.getRuleInfo();
        another.setEnable(true);
        Assertions.assertEquals(ruleInfo, another);
        Assertions.assertFalse(ruleInfo.isEnable());
    }
}
