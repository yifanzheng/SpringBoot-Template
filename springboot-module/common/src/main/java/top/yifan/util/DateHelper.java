package top.yifan.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.Format;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * DateHelper
 */
public class DateHelper {

    private DateHelper() {
    }

    public static String format(Instant date) {
        return format(date.toEpochMilli());
    }

    public static String format(Long millis) {
        if (millis == null) {
            return null;
        }
        return getDateFormat().format(millis);
    }

    public static Format getDateFormat() {
        return DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT;
    }
}
