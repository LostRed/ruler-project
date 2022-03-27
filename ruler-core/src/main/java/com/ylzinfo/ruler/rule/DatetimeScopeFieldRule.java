package com.ylzinfo.ruler.rule;

import com.ylzinfo.ruler.annotation.Rule;
import com.ylzinfo.ruler.core.ValidConfiguration;
import com.ylzinfo.ruler.domain.Report;
import com.ylzinfo.ruler.domain.RuleInfo;
import com.ylzinfo.ruler.domain.ValidInfo;
import com.ylzinfo.ruler.utils.DatetimeUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@Rule(ruleCode = "datetime_scope", businessType = "common", desc = "规定的日期时间字段必须在限定的范围内")
public class DatetimeScopeFieldRule<E> extends SingleFieldRule<E> {

    public DatetimeScopeFieldRule(ValidConfiguration validConfiguration, RuleInfo ruleInfo) {
        super(validConfiguration, ruleInfo);
    }

    @Override
    public boolean isSupported(E element) {
        return !validConfiguration.getDatetimeScopeValidInfos().isEmpty();
    }

    @Override
    public boolean judge(E element) {
        return validConfiguration.getDatetimeScopeValidInfos().stream()
                .anyMatch(validInfo -> this.check(element, validInfo));
    }

    @Override
    public Report buildReport(E element) {
        Map<String, Object> map = validConfiguration.getDatetimeScopeValidInfos().stream()
                .flatMap(validInfo -> this.collectIllegals(element, validInfo).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this.getReport(this.ruleInfo, element, map);
    }

    @Override
    protected boolean isNotMatch(ValidInfo validInfo, Object value) {
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
    protected Set<Map.Entry<String, Object>> wrap(ValidInfo validInfo, Object value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginTime = validInfo.getBeginTime();
        LocalDateTime endTime = validInfo.getEndTime();
        String lower = beginTime == null ? "-∞" : formatter.format(beginTime);
        String upper = endTime == null ? "+∞" : formatter.format(endTime);
        value = formatter.format((LocalDateTime) value) + " (参考值: " + lower + " ~ " + upper + ")";
        return super.wrap(validInfo, value);
    }
}
