package life.chenshi.keepaccounts.module.common.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyy年MM月");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy年");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat ALL_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat MONTH_DAY_FORMAT_2 = new SimpleDateFormat("MM/dd");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat HOUR_MINUTE = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_DAY_HOUR_MIN_FORMAT = new SimpleDateFormat("yyyy/MM/dd - HH:mm");

    public static final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

    /**
     * 时间字符串转化为 Date 对象
     *
     * @param time   时间字符串
     * @param format DateFormat 格式化类
     * @return Date 对象
     */
    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Date 对象转化为 时间字符串
     *
     * @param date   Date 对象
     * @param format DateFormat 格式化类
     * @return 时间字符串
     */
    public static String date2String(Date date, DateFormat format) {
        return format.format(date);
    }

    /**
     * Date 对象转化为xx月xx日格式字符串
     *
     * @param date Date 对象
     * @return xx月xx日 字符串
     */
    public static String date2MonthDay(Date date) {
        return date2String(date, MONTH_DAY_FORMAT);
    }

    /**
     * 判断两个 Date 是否是同一天
     *
     * @param date1 Date 对象
     * @param date2 Date 对象
     * @return true:同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);

        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    /**
     * 获取某月份开始时刻的 Date
     *
     * @return 当前月份开始的 Date
     */
    public static Date getMonthStart(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某月份结束时刻的 Date
     *
     * @return 当前月份结束的 Date
     */
    public static Date getMonthEnd(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
        int maxSecond = calendar.getActualMaximum(Calendar.SECOND);
        int maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        calendar.set(Calendar.MINUTE, maxMinute);
        calendar.set(Calendar.SECOND, maxSecond);
        calendar.set(Calendar.MILLISECOND, maxMillisecond);

        return calendar.getTime();
    }

    /**
     * 获取当前月份开始时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-01T00:00:00.000+0800
     *
     * @return 当前月份开始的 Date
     */
    public static Date getCurrentMonthStart() {
        return getMonthStart(System.currentTimeMillis());
    }

    /**
     * 获取当前月份结束时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-30T23:59:59.999+0800
     *
     * @return 当前月份结束的 Date
     */
    public static Date getCurrentMonthEnd() {
        return getMonthEnd(System.currentTimeMillis());
    }

    /**
     * 获取当前月份开始时间戳
     * 比如当前是 2018年4月24日
     * 返回的时间是 2018年4月24日 零点整时间戳
     *
     * @return 当前月份开始时间戳
     */
    public static long getTodayStartMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前月份结束时间戳
     * 比如当前是 2018年4月24日
     * 返回的时间是 2018年4月24日 23:59:59:999
     *
     * @return 当前月份结束时间戳
     */
    public static long getTodayEndMillis() {
        Calendar calendar = Calendar.getInstance();
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
        int maxSecond = calendar.getActualMaximum(Calendar.SECOND);
        int maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND);

        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        calendar.set(Calendar.MINUTE, maxMinute);
        calendar.set(Calendar.SECOND, maxSecond);
        calendar.set(Calendar.MILLISECOND, maxMillisecond);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取一个月中的天数
     */
    public static int getDaysInMonth(long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getDayInThisMonth() {
        LocalDate localDate = LocalDate.now();
        return localDate.getDayOfMonth();
    }

    /**
     * 获取今年开始date
     */
    public static Date getCurrentYearStart() {
        return getYearStart(System.currentTimeMillis());
    }

    /**
     * 根据时间戳获取某年的开始date
     */
    public static Date getYearStart(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取今年结束date
     */
    public static Date getCurrentYearEnd() {
        return getYearEnd(System.currentTimeMillis());
    }

    /**
     * 根据时间戳获取某年的结束date
     */
    public static Date getYearEnd(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        // 获取一年最大的天数
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
        int maxSecond = calendar.getActualMaximum(Calendar.SECOND);
        int maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_YEAR, maxDay);
        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        calendar.set(Calendar.MINUTE, maxMinute);
        calendar.set(Calendar.SECOND, maxSecond);
        calendar.set(Calendar.MILLISECOND, maxMillisecond);
        return calendar.getTime();
    }

    /**
     * 参数位数为13位
     */
    public static long getDaysBetween(long a, long b) {

        Date date1 = new Date(a);
        Date date2 = new Date(b);

        String d1 = date2String(date1, YEAR_MONTH_DAY_FORMAT);
        String d2 = date2String(date2, YEAR_MONTH_DAY_FORMAT);

        date1 = string2Date(d1, YEAR_MONTH_DAY_FORMAT);
        date2 = string2Date(d2, YEAR_MONTH_DAY_FORMAT);

        long diff = Math.abs(date1.getTime() - date2.getTime());

        return diff / MILLIS_IN_DAY;
    }
}
