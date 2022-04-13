package info.lostred.ruler.rule;

import info.lostred.ruler.annotation.Rule;
import info.lostred.ruler.core.GlobalConfiguration;
import info.lostred.ruler.domain.Report;
import info.lostred.ruler.domain.RuleInfo;
import info.lostred.ruler.domain.ValidInfo;
import info.lostred.ruler.util.DatetimeUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 日期时间范围字段校验规则
 *
 * @param <E> 规则约束的参数类型
 * @author dengluwei
 */
@Rule(ruleCode = "datetime_scope", desc = "规定的日期时间字段必须在限定的范围内")
public class DateTimeScopeFieldRule<E> extends ScopeFieldRule<E> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateTimeScopeFieldRule(GlobalConfiguration globalConfiguration, RuleInfo ruleInfo) {
        super(globalConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !globalConfiguration.getDateTimeScopeValidInfos().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return globalConfiguration.getDateTimeScopeValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = globalConfiguration.getDateTimeScopeValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Report.of(ruleInfo).putIllegals(map);
    }

    @Override
    protected boolean isIllegal(ValidInfo validInfo, Object value) {
        if (value instanceof Date) {
            Instant instant = ((Date) value).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            return this.notInTimeScope(localDateTime, validInfo);
        } else if (value instanceof LocalDate) {
            LocalDateTime localDateTime = ((LocalDate) value).atStartOfDay();
            return this.notInTimeScope(localDateTime, validInfo);
        } else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return this.notInTimeScope(localDateTime, validInfo);
        } else if (value instanceof String) {
            LocalDateTime localDateTime = DatetimeUtils.format(value.toString());
            return this.notInTimeScope(localDateTime, validInfo);
        }
        return false;
    }

    /**
     * 不在时间范围内
     *
     * @param localDateTime 日期时间
     * @param validInfo     校验信息
     * @return 不在返回true，否则返回false
     */
    private boolean notInTimeScope(LocalDateTime localDateTime, ValidInfo validInfo) {
        LocalDateTime beginTime = validInfo.getBeginTime() == null ?
                LocalDateTime.MIN : validInfo.getBeginTime();
        LocalDateTime endTime = validInfo.getEndTime() == null ?
                LocalDateTime.MAX : validInfo.getEndTime();
        return localDateTime.isBefore(beginTime)
                || localDateTime.isAfter(endTime);
    }

    @Override
    protected Set<Map.Entry<String, Object>> wrap(E element, ValidInfo validInfo, Object value) {
        LocalDateTime localDateTime = null;
        if (value instanceof Date) {
            Instant instant = ((Date) value).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            localDateTime = instant.atZone(zoneId).toLocalDateTime();
        } else if (value instanceof LocalDate) {
            localDateTime = ((LocalDate) value).atStartOfDay();
        } else if (value instanceof LocalDateTime) {
            localDateTime = (LocalDateTime) value;
        } else if (value instanceof String) {
            localDateTime = DatetimeUtils.format(value.toString());
        }
        assert localDateTime != null;
        String format;
        String lower;
        String upper;
        LocalDateTime beginTime = validInfo.getBeginTime();
        LocalDateTime endTime = validInfo.getEndTime();
        if (localDateTime.isEqual(localDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime())) {
            format = this.dateFormatter.format(localDateTime);
            lower = beginTime == null ? null : this.dateFormatter.format(beginTime);
            upper = endTime == null ? null : this.dateFormatter.format(endTime);
        } else {
            format = this.dateTimeFormatter.format(localDateTime);
            lower = beginTime == null ? null : this.dateTimeFormatter.format(beginTime);
            upper = endTime == null ? null : this.dateTimeFormatter.format(endTime);
        }
        return super.wrap(element, validInfo, this.appendReference(format, lower, upper));
    }
}
