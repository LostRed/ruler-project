package info.lostred.ruler.test.domain;

import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.Result;
import info.lostred.ruler.domain.RuleInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ResultTest {
    @Test
    void should_get_another_report_list() {
        Result result = Result.of();
        result.addReport(Report.of(new RuleInfo()));
        List<Report> reports = result.getReports();
        List<Report> another = result.getReports();
        Assertions.assertEquals(1, reports.size());
        Assertions.assertEquals(1, another.size());
        Assertions.assertEquals(another, reports);
    }
}
