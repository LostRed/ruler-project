package info.lostred.ruler.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具
 *
 * @author lostred
 */
public final class DatetimeUtils {
    /**
     * 将日期字符串转换为标准化日期
     *
     * @param date 日期字符串
     * @return 日期
     */
    public static LocalDate formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date = standardized(date).trim();
        if ("".equals(date)) {
            return null;
        }
        // yyyyMMdd
        if (date.matches("^\\d{4}\\d{2}\\d{2}$")) {
            String regex = "(\\d{4})(\\d{2})(\\d{2})";
            date = date.replaceAll(regex, "$1-$2-$3");
            return LocalDate.parse(date, formatter);
        }
        // yyyyMMddHHmmss
        else if (date.matches("^\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}$")) {
            String regex = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
            date = date.replaceAll(regex, "$1-$2-$3");
            return LocalDate.parse(date, formatter);
        }
        // yyyy-MM-dd
        else if (date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return LocalDate.parse(date, formatter);
        }
        // yyyy-MM-dd HH:mm:ss
        else if (date.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
            String regex = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})";
            date = date.replaceAll(regex, "$1-$2-$3");
            return LocalDate.parse(date, formatter);
        }
        // yyyy-MM-ddTHH:mm:ss
        else if (date.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
            String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})";
            date = date.replaceAll(regex, "$1-$2-$3");
            return LocalDate.parse(date, formatter);
        } else {
            throw new IllegalArgumentException("Illegal date format: '" + date + "'");
        }
    }

    /**
     * 将日期时间字符串转换为标准化日期时间
     *
     * @param dateTime 日期时间字符串
     * @return 日期时间
     */
    public static LocalDateTime formatDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateTime = standardized(dateTime).trim();
        if ("".equals(dateTime)) {
            return null;
        }
        // yyyyMMddHHmmss
        if (dateTime.matches("^\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}$")) {
            String regex = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
            dateTime = dateTime.replaceAll(regex, "$1-$2-$3 $4:$5:$6");
            return LocalDateTime.parse(dateTime, formatter);
        }
        // yyyy-MM-dd HH:mm:ss
        else if (dateTime.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
            return LocalDateTime.parse(dateTime, formatter);
        }
        // yyyy-MM-ddTHH:mm:ss
        else if (dateTime.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
            String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})";
            dateTime = dateTime.replaceAll(regex, "$1-$2-$3 $4:$5:$6");
            return LocalDateTime.parse(dateTime, formatter);
        } else {
            throw new IllegalArgumentException("Illegal datetime format: '" + dateTime + "'");
        }
    }

    /**
     * 将字符串转换为标准化日期时间
     *
     * @param string 字符串
     * @return 日期时间
     */
    public static LocalDateTime format(String string) {
        try {
            return formatDateTime(string);
        } catch (IllegalArgumentException e) {
            try {
                LocalDate localDate = formatDate(string);
                if (localDate != null) {
                    return localDate.atStartOfDay();
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Illegal format: '" + string + "'");
    }

    /**
     * 标准化日期分隔符
     * <p>将"/"和"."替换为"-"</p>
     *
     * @param source 源字符串
     * @return 替换后的字符串
     */
    public static String standardized(String source) {
        StringBuilder sb = new StringBuilder();
        char[] chars = source.toCharArray();
        for (char aChar : chars) {
            if (aChar == '/' || aChar == '.') {
                sb.append('-');
            } else {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }
}
