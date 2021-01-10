package life.chenshi.keepaccounts.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static final DateFormat HOUR_MINUTE = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");


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
     * 获取当前年月
     */
    public static String getCurrentYearMonth() {
        Calendar calendar = Calendar.getInstance();
        return date2String(calendar.getTime(), YEAR_MONTH_FORMAT);
    }

    /**
     * 格式化年月
     *
     * @param year  年份
     * @param month 月份（字面）
     */
    public static String getYearMonthFormatString(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return date2String(calendar.getTime(), YEAR_MONTH_FORMAT);
    }

    /**
     * 获取当前年份
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取修正后的当前月份
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某月有多少天
     *
     * @param year  年份
     * @param month 月份
     * @return 该月的天数
     */
    public static int getDayCount(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某月份开始时刻的 Date
     *
     * @param year  年份
     * @param month 月份
     * @return 当前月份开始的 Date
     */
    public static Date getMonthStart(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
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
     * @param year  年份
     * @param month 月份
     * @return 当前月份结束的 Date
     */
    public static Date getMonthEnd(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
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
        Calendar calendar = Calendar.getInstance();
        return getMonthStart(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取当前月份结束时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-30T23:59:59.999+0800
     *
     * @return 当前月份结束的 Date
     */
    public static Date getCurrentMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        return getMonthEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
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
    public static int getDaysInMonth(long millisecond){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
