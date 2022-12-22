package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * <br>CreateDate 六月 16,2022
 * @author peijie.ye
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER0 = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final FastDateFormat FAST_DATE_TIME_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static final FastDateFormat FAST_DATE_TIME_0_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");
    private static final FastDateFormat FAST_DATE_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd");
    private static final FastDateFormat FAST_MONTH_FORMATTER = FastDateFormat.getInstance("yyyy-MM");
    private static final FastDateFormat FAST_JSON_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            TimeZone.getTimeZone("GMT+0"));
    private static final FastDateFormat[] FAST_DATE_FORMATS = {FAST_DATE_TIME_FORMATTER, FAST_DATE_FORMATTER,
            FAST_DATE_TIME_0_FORMATTER, FAST_JSON_FORMATTER, FAST_MONTH_FORMATTER};
    // 无效值
    public static final LocalDateTime $INVALID = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);

    public static boolean isValid(Date date) {
        return date != null && date.getTime() != 0;
    }

    public static boolean isValid(LocalDateTime date) {
        return date != null && !$INVALID.equals(date);
    }

    public static boolean isValid(LocalDate date) {
        return date != null && !$INVALID.toLocalDate().equals(date);
    }

    public static boolean isInvalid(Date date) {
        return !isValid(date);
    }

    public static boolean isInvalid(LocalDateTime date) {
        return date == $INVALID || !isValid(date);
    }

    public static boolean isInvalid(LocalDate date) {
        return !isValid(date);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    public static Date nowDate() {
        return new Date();
    }

    public static LocalDateTime defaultIfNull(LocalDateTime time) {
        return time == null ? LocalDateTime.now() : time;
    }

    public static LocalDate defaultIfNull(LocalDate time) {
        return time == null ? LocalDate.now() : time;
    }

    public static Date defaultIfNull(Date time) {
        return time == null ? new Date() : time;
    }

    public static long defaultIfNull(Long milli) {
        return milli == null ? System.currentTimeMillis() : milli;
    }

    public static LocalDateTime of(int year, int month, int day) {
        return of(year, month, day, 0, 0, 0);
    }

    public static LocalDateTime of(int year, int month, int day, int hour, int minute, int second) {
        return of(year, month, day, hour, minute, second, 0);
    }

    public static LocalDateTime of(int year, int month, int day, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, day, hour, minute, second, nanoOfSecond);
    }

    public static LocalDateTime of(String date) {
        return parse(date);
    }

    public static LocalDateTime of(long milli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    public static LocalDateTime of(Long milli) {
        return milli == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    public static Date ofDate(long date) {
        return new Date(date);
    }

    public static Date ofDate(Long date) {
        return date == null ? null : new Date(date);
    }

    public static Date ofDate(LocalDate date) {
        return date == null ? null : ofDate(getTimestamp(date));
    }

    public static Date ofDate(LocalDateTime date) {
        return date == null ? null : ofDate(getTimestamp(date));
    }

    public static LocalDateTime of(Date date) {
        return date == null ? null : of(date.getTime());
    }

    public static LocalDateTime of(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    public static LocalDate ofLocalDate(String date) {
        return parseAsLocalDate(date);
    }

    public static LocalDate ofLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static LocalDate ofLocalDate(LocalDateTime date) {
        return date == null ? null : date.toLocalDate();
    }

    public static LocalDate ofLocalDate(Date date) {
        return date == null ? null : ofLocalDate(date.getTime());
    }

    public static LocalDate ofLocalDate(long milli) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    public static LocalDate ofLocalDate(Long milli) {
        return milli == null ? null : LocalDate.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    public static String format(long time, String format) {
        return StringUtils.isEmpty(format) ?
                FAST_DATE_TIME_FORMATTER.format(time) :
                FastDateFormat.getInstance(format).format(time);
    }

    public static String format(Long time, String format) {
        if (time == null) {
            return null;
        } else if (StringUtils.isEmpty(format)) {
            return FAST_DATE_TIME_FORMATTER.format(time);
        } else {
            return FastDateFormat.getInstance(format).format(time);
        }
    }

    public static String format(Date time, String format) {
        if (time == null) {
            return null;
        } else if (StringUtils.isEmpty(format)) {
            return FAST_DATE_TIME_FORMATTER.format(time);
        } else {
            return FastDateFormat.getInstance(format).format(time);
        }
    }

    public static String format(LocalDateTime time, String format) {
        if (time == null) {
            return null;
        } else if (StringUtils.isEmpty(format)) {
            return DATE_TIME_FORMATTER.format(time);
        } else {
            return format(getTimestamp(time), format);
        }
    }

    public static String format(LocalDate time, String format) {
        if (time == null) {
            return null;
        } else if (StringUtils.isEmpty(format)) {
            return DATE_TIME_FORMATTER0.format(time);
        } else {
            return format(getTimestamp(time), format);
        }
    }

    public static String format(long time) {
        return FAST_DATE_TIME_FORMATTER.format(time);
    }

    public static String format(Long time) {
        return time == null ? null : FAST_DATE_TIME_FORMATTER.format(time);
    }

    public static String format(LocalDate time) {
        if (time == null) {return null;}
        return DATE_TIME_FORMATTER0.format(time);
    }

    public static String format(LocalDateTime time) {
        if (time == null) {return null;}
        return DATE_TIME_FORMATTER.format(time);
    }

    public static String format(TemporalAccessor time) {
        if (time == null) {return null;}
        if (time instanceof LocalDate) {return DATE_TIME_FORMATTER0.format(time);}
        return DATE_TIME_FORMATTER.format(time);
    }

    public static String format(Date time) {
        return time == null ? null : FAST_DATE_TIME_FORMATTER.format(time);
    }

    public static String formatDate(TemporalAccessor time) {
        return time == null ? null : DATE_FORMATTER.format(time);
    }

    public static int formatDateAsNum(Date time) {
        if (time == null) {
            return 0;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            return calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(
                    Calendar.DAY_OF_MONTH);
        }
    }

    public static int formatDateAsNum(TemporalAccessor time) {
        if (time == null) {
            return 0;
        } else {
            return time.get(ChronoField.YEAR) * 10000 + time.get(ChronoField.MONTH_OF_YEAR) * 100 + time.get(
                    ChronoField.DAY_OF_MONTH);
        }
    }

    public static String formatDate(Date time) {
        return time == null ? null : FAST_DATE_FORMATTER.format(time);
    }

    public static String formatMonth(Date time) {
        return time == null ? null : FAST_MONTH_FORMATTER.format(time);
    }

    public static String formatMonth(TemporalAccessor time) {
        return time == null ? null : MONTH_FORMATTER.format(time);
    }

    public static int formatMonthAsNum(Date time) {
        if (time == null) {
            return 0;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            return calendar.get(Calendar.YEAR) * 100 + (calendar.get(Calendar.MONTH) + 1);
        }
    }

    public static int formatMonthAsNum(TemporalAccessor time) {
        if (time == null) {
            return 0;
        } else {
            return time.get(ChronoField.YEAR) * 100 + time.get(ChronoField.MONTH_OF_YEAR);
        }
    }

    public static Date parseAsDate(String date) {
        if (StringUtils.isEmpty(date)) {return null;}
        for (FastDateFormat format : FAST_DATE_FORMATS) {
            try {return format.parse(date);} catch (Throwable ignored) {}//NOSONAR
        }
        try {return new Date(Long.parseLong(date));} catch (Throwable ignored) {}// NOSONAR
        return null;
    }

    public static LocalDate parseAsLocalDate(String date) {
        return ofLocalDate(parseAsDate(date));
    }

    public static LocalDateTime parse(String date) {
        return of(parseAsDate(date));
    }

    public static long getTimestamp(LocalDate time) {
        return time == null ?
                Long.MIN_VALUE :
                time.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getTimestamp(LocalDateTime time) {
        return time == null ? Long.MIN_VALUE : time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getTimestamp(Date time) {
        return time == null ? Long.MIN_VALUE : time.getTime();
    }

    public static LocalDate addDay(LocalDate time, int i) {
        return time == null ? null : time.plusDays(i);
    }

    public static LocalDateTime addDay(LocalDateTime time, int i) {
        return time == null ? null : time.plusDays(i);
    }

    public static int getYear(LocalDateTime date) {
        return date == null ? Integer.MIN_VALUE : date.getYear();
    }

    public static int getYear(LocalDate date) {
        return date == null ? Integer.MIN_VALUE : date.getYear();
    }

    public static int getYear(Date date) {
        if (date == null) {return Integer.MIN_VALUE;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(LocalDateTime date) {
        return date == null ? Integer.MIN_VALUE : date.getMonthValue();
    }

    public static int getMonth(LocalDate date) {
        return date == null ? Integer.MIN_VALUE : date.getMonthValue();
    }

    public static int getMonth(Date date) {
        if (date == null) {return Integer.MIN_VALUE;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth(LocalDateTime date) {
        return date == null ? Integer.MIN_VALUE : date.getDayOfMonth();
    }

    public static int getDayOfMonth(LocalDate date) {
        return date == null ? Integer.MIN_VALUE : date.getDayOfMonth();
    }

    public static int getDayOfMonth(Date date) {
        if (date == null) {return Integer.MIN_VALUE;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * mysql 对应是 %v
     * @param date
     * @return
     */
    public static int getWeekOfYear(LocalDate date) {
        return date == null ? -1 : date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    /**
     * mysql 对应是 %v
     * @param date
     * @return
     */
    public static int getWeekOfYear(LocalDateTime date) {
        return date == null ? -1 : date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    /**
     * mysql 对应是 %v
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        return date == null ? -1 : of(date).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    public static LocalDate atStartOfYear(LocalDate time) {
        return time == null ? null : LocalDate.of(time.getYear(), 1, 1);
    }

    public static LocalDate atEndOfYear(LocalDate time) {
        return time == null ? null : LocalDate.of(time.getYear(), 12, 31);
    }

    public static LocalDate atStartOfMonth(LocalDate time) {
        return time == null ? null : LocalDate.of(time.getYear(), time.getMonth(), 1);
    }

    public static LocalDate atEndOfMonth(LocalDate time) {
        return time == null ? null : time.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime atStartOfYear(LocalDateTime time) {
        return time == null ? null : LocalDateTime.of(time.getYear(), 1, 1, 0, 0, 0, 0);
    }

    public static LocalDateTime atEndOfYear(LocalDateTime time) {
        return time == null ? null : LocalDateTime.of(time.getYear(), 12, 31, 23, 59, 59, 999_999_999);
    }

    public static LocalDateTime atStartOfMonth(LocalDateTime time) {
        return time == null ? null : LocalDateTime.of(time.getYear(), time.getMonth(), 1, 0, 0, 0, 0);
    }

    public static LocalDateTime atEndOfMonth(LocalDateTime time) {
        return time == null ? null : atEndOfDay(time.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static LocalDateTime atStartOfDay(LocalDateTime time) {
        return time == null ?
                null :
                LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 0, 0, 0, 0);
    }

    public static LocalDateTime atEndOfDay(LocalDateTime time) {
        return time == null ?
                null :
                LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59, 999_999_999);
    }


    public static int compare(LocalDateTime time1, LocalDateTime time2) {
        if (time1 == time2) {return 0;}
        if (time1 == null) {return -1;}
        if (time2 == null) {return 1;}
        return time1.compareTo(time2);
    }

    public static int compare(LocalDate time1, LocalDate time2) {
        if (time1 == time2) {return 0;}
        if (time1 == null) {return -1;}
        if (time2 == null) {return 1;}
        return time1.compareTo(time2);
    }

    public static int compare(Date time1, Date time2) {
        if (time1 == time2) {return 0;}
        if (time1 == null) {return -1;}
        if (time2 == null) {return 1;}
        return time1.compareTo(time2);
    }

    public static LocalDateTime max(LocalDateTime time1, LocalDateTime time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) > 0 ? time1 : time2;
    }

    public static LocalDateTime min(LocalDateTime time1, LocalDateTime time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) < 0 ? time1 : time2;
    }

    public static LocalDate max(LocalDate time1, LocalDate time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) > 0 ? time1 : time2;
    }

    public static LocalDate min(LocalDate time1, LocalDate time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) < 0 ? time1 : time2;
    }

    public static Date max(Date time1, Date time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) > 0 ? time1 : time2;
    }

    public static Date min(Date time1, Date time2) {
        if (time1 == null) {return time2;}
        if (time2 == null) {return time1;}
        return compare(time1, time2) < 0 ? time1 : time2;
    }

    public static boolean lt(LocalDateTime time1, LocalDateTime time2) {
        return compare(time1, time2) < 0;
    }

    public static boolean lte(LocalDateTime time1, LocalDateTime time2) {
        return compare(time1, time2) < 1;
    }

    public static boolean gt(LocalDateTime time1, LocalDateTime time2) {
        return compare(time1, time2) > 0;
    }

    public static boolean gte(LocalDateTime time1, LocalDateTime time2) {
        return compare(time1, time2) > -1;
    }

    public static boolean lt(LocalDate time1, LocalDate time2) {
        return compare(time1, time2) < 0;
    }

    public static boolean lte(LocalDate time1, LocalDate time2) {
        return compare(time1, time2) < 1;
    }

    public static boolean gt(LocalDate time1, LocalDate time2) {
        return compare(time1, time2) > 0;
    }

    public static boolean gte(LocalDate time1, LocalDate time2) {
        return compare(time1, time2) > -1;
    }

    public static boolean lt(Date time1, Date time2) {
        return compare(time1, time2) < 0;
    }

    public static boolean lte(Date time1, Date time2) {
        return compare(time1, time2) < 1;
    }

    public static boolean gt(Date time1, Date time2) {
        return compare(time1, time2) > 0;
    }

    public static boolean gte(Date time1, Date time2) {
        return compare(time1, time2) > -1;
    }

    public static int diffDay(Date leftTime, Date rightTime) {
        if (leftTime == null || rightTime == null) {return Integer.MIN_VALUE;}
        return diffDay(leftTime.getTime(), rightTime.getTime());
    }

    public static int diffDay(LocalDate leftTime, LocalDate rightTime) {
        if (leftTime == null || rightTime == null) {return Integer.MIN_VALUE;}
        return (int) leftTime.until(rightTime, ChronoUnit.DAYS);
    }

    public static int diffDay(LocalDateTime leftTime, LocalDateTime rightTime) {
        if (leftTime == null || rightTime == null) {return Integer.MIN_VALUE;}
        return (int) leftTime.until(rightTime, ChronoUnit.DAYS);
    }

    public static int diffDay(long leftTime, long rightTime) {
        int offset = TimeZone.getDefault().getRawOffset();
        leftTime += offset;
        rightTime += offset;
        return (int) ((leftTime / 86400000L) - (rightTime / 86400000L));
    }
}
