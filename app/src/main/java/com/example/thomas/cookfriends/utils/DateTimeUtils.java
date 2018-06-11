package com.example.thomas.cookfriends.utils;

/**
 * Created by tarena on 2017/7/5.
 */


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    /**
     * 格式化时间的工具
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    /**
     * 被格式化的时间对象
     */
    private static Date date = new Date();
    /**
     * 默认的模式字符串
     */
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取应用于播放器上的时间的字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为 mm:ss 格式的字符串
     */
    public static String getPlayerTime(long millis) {
        if (millis >= 1 * 60 * 60 * 1000) {
            return getPlayerLongTime(millis);
        } else {
            return getPlayerShortTime(millis);
        }
    }

    /**
     * 获取应用于播放器上的时间的字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为 mm:ss 格式的字符串
     */
    public static String getPlayerShortTime(long millis) {
        return getFormattedTime(millis, "mm:ss");
    }

    /**
     * 获取应用于播放器上的时间的字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为 HH:mm:ss 格式的字符串
     */
    public static String getPlayerLongTime(long millis) {
        return getFormattedTime(millis - 8 * 60 * 60 * 1000, "HH:mm:ss");
    }

    /**
     * 将long类型的时间戳格式化为"yyyy-MM-dd HH:mm:ss"模式的String
     *
     * @param millis 需要被格式化的时间的时间戳
     * @return 格式化为"yyyy-MM-dd HH:mm:ss"模式的String
     */
    public static String getFormattedTime(long millis) {
        return getFormattedTime(millis, PATTERN_DEFAULT);
    }

    /**
     * 将long类型的时间戳格式化为指定模式的String
     *
     * @param millis  需要被格式化的时间的时间戳
     * @param pattern 格式化时使用的模式字符串，例如：{@link #PATTERN_DEFAULT}
     * @return 格式化为pattern模式的String
     */


    /**
     * 当天------HH:mm:ss
     * 昨天 -----昨天 HH:mm:ss
     * 一周以内 -----星期X
     * 一周以前 ------ yyyy-MM-dd
     *
     * @param millis
     * @param pattern
     * @return
     */

    public static String getFormattedTime(long millis, String pattern) {
        sdf.applyPattern(pattern);
        date.setTime(millis);
        return sdf.format(date);
    }


    public static int dayDiff(long stamp) {

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(stamp);
        int dayDiff = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR);


       /* long diff = System.currentTimeMillis() - stamp;
        int days = (int) diff / (1000 * 60 * 60 * 24);*/
        return dayDiff;
    }

    public static String refinedFormatDate(long stamp) {
        String formated = null;
        String pattern = null;
        int days = dayDiff(stamp);


        if (days == 0) {
            pattern = "HH:mm:ss";
            formated = getFormattedTime(stamp, pattern);
        } else if (days == 1) {
            pattern = "HH:mm:ss";
            formated = "昨天 " + getFormattedTime(stamp, pattern);

        } else if (days > 1) {
            pattern = "yyyy-MM-dd";
            formated = getFormattedTime(stamp, pattern);
        }

        return formated;
    }


    private static String getDiffHours(long stamp) {

        long diffMillies = System.currentTimeMillis() - stamp;
        int hours = (int) diffMillies / (1000 * 60 * 60);


        return hours > 0 ? hours + "小时以前" : "刚刚";
    }

    public static String formatDate(long stamp) {
        String formated = null;
        String pattern = null;
        int days = dayDiff(stamp);


        if (days == 0) {
            pattern = "HH:mm:ss";
            formated = getDiffHours(stamp);
        } else if (days == 1) {
            pattern = "HH:mm:ss";
            formated = "昨天 " + getFormattedTime(stamp, pattern);
        } else if (days <= 7 && days > 1) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(stamp);
            int week = c.get(Calendar.DAY_OF_WEEK);

            switch (week) {
                case Calendar.MONDAY:
                    pattern = "星期一";
                    break;
                case Calendar.TUESDAY:
                    pattern = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    pattern = "星期三";
                    break;
                case Calendar.THURSDAY:
                    pattern = "星期四";
                    break;
                case Calendar.FRIDAY:
                    pattern = "星期五";
                    break;
                case Calendar.SATURDAY:
                    pattern = "星期六";
                    break;
                case Calendar.SUNDAY:
                    pattern = "星期日";
                    break;

            }

        } else if (days > 7) {
            pattern = "yyyy-MM-dd";
            formated = getFormattedTime(stamp, pattern);
        }


        return formated;
    }


}
