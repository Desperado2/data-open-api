package com.github.desperado2.data.open.api.common.manage.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期操作类
 * @author tu nan
 * @date 2021/3/11
 **/
public class DateUtils {

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式：yyyyMMddHHmmss
     */
    public static final String DATE_TO_SECOND_STRING = "yyyyMMddHHmmss";
    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DATE_SHORT = "yyyy-MM-dd";

    public static final String DATE_SHORT_POINT_DELIMITER = "yyyy.MM.dd";
    /**
     * 日期格式：yyyy-MM
     */
    public static final String MONTH_SHORT = "yyyy-MM";


    public static final String MONTH_DAY = "MM-dd";
    /**
     * 日期格式 yyyyMMdd
     */
    public static final String DATE_NORMAL = "yyyyMMdd";

    public static final int  TWENTY_FOUR = 24;

    /**
     * 获取当前时间字符串
     * @return 当前时间字符串
     */
    public static String getNowString() {
        return dateFormat(new Date());
    }

    /**
     * 从时间格式化字符串
     * 默认格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return yyyy-MM-dd HH:mm:ss格式的时间
     */
    public static String dateFormat(Date date) {
        return dateFormat(date, DATE_LONG);
    }

    /**
     * 从时间格式化字符串
     *
     * @param date 时间
     * @param format 时间格式
     * @return 对应格式的时间
     */
    public static String dateFormat(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null || "".equals(format)) {
            format = DATE_LONG;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 从字符串格式化时间
     *
     * @param dateStr 时间字符串
     * @param format 时间格式
     * @return 时间
     */
    public static Date dateFromString(String dateStr, String format) {
        if (!"".equals(dateStr) && !"".equals(format)) {
            DateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    /**
     * 获取当前时间，格式为年月日时分秒，去掉毫秒值
     * @return 时间
     */
    public static Date getCurrentTime() {
        Date date = new Date();
        String cleanDateStr = dateFormat(date, DATE_LONG);
        return dateFromString(cleanDateStr, DATE_LONG);
    }

    /**
     * 获取当前时间，格式为年月日时分秒，去掉毫秒值
     * @return 时间
     */
    public static long getCurrentTime(Long subMillSeconds) {
        String cleanDateStr = dateFormat( new Date(), DATE_LONG);
        Date date = dateFromString(cleanDateStr, DATE_LONG);
        if(date == null){
            return 0L;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        localDateTime = localDateTime.plusSeconds(subMillSeconds / 1000);
        return  localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }


    /**
     * 获取当前时间，格式为年月日时分秒，去掉毫秒值
     * @return 时间
     */
    public static Date addHours(Date date,int hours) {
        return  org.apache.commons.lang3.time.DateUtils.addHours(date, hours);
    }


    /**
     * 获取LocalDate
     * @param date 日期
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * 获取一天的24个小时
     * @return 时间列表
     */
    public static List<String> getOneDayAllHours(){
        List<String> result = new ArrayList<>();
        for (int i=0; i< TWENTY_FOUR; i++){
            //确定格式，把1转换为001
            DecimalFormat mFormat = new DecimalFormat("00");
            String no = mFormat.format(i);
            result.add(no+":00");
        }
        return result;
    }


    /**
     * 获取时间段的每一天
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pattern 时间格式
     * @return 时间列表
     */
    public static List<String> getDayRange(Date startDate, Date endDate, String pattern) {
        List<String> list = new ArrayList<>();
        if(startDate == null || endDate == null || StringUtils.isBlank(pattern)){
            return list;
        }
        while (startDate.before(endDate)) {
            list.add(new SimpleDateFormat(pattern).format(startDate));
            startDate = org.apache.commons.lang3.time.DateUtils.addDays(startDate, 1);
        }
        list.add(new SimpleDateFormat(pattern).format(endDate));
        return list;
    }


    /**
     * 获取时间段的每一周
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pattern 时间格式
     * @return 时间列表
     */
    public static List<String> getWeekRange(Date startDate, Date endDate, String pattern) {
        List<String> list = new ArrayList<>();
        if(startDate == null || endDate == null || StringUtils.isBlank(pattern)){
            return list;
        }
        while (startDate.before(endDate)) {
            list.add(new SimpleDateFormat(pattern).format(startDate));
            startDate = org.apache.commons.lang3.time.DateUtils.addWeeks(startDate, 1);
        }
        if(startDate.equals(endDate)){
            list.add(new SimpleDateFormat(pattern).format(endDate));
        }
        return list;
    }


    /**
     * 获取时间段的每一月
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pattern 时间格式
     * @return 时间列表
     */
    public static List<String> getMonthRange(Date startDate, Date endDate, String pattern) {
        List<String> list = new ArrayList<>();
        if(startDate == null || endDate == null || StringUtils.isBlank(pattern)){
            return list;
        }
        while (startDate.before(endDate)) {
            list.add(new SimpleDateFormat(pattern).format(startDate));
            startDate = org.apache.commons.lang3.time.DateUtils.addMonths(startDate, 1);
        }
        list.add(new SimpleDateFormat(pattern).format(endDate));
        return list;
    }


    /**
     * 获取时间段的每一月
     * @param startDate 开始时间
     * @return 时间列表
     */
    public static Date getNewDate(Date startDate, Integer number, String type) {
        if("second".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addSeconds(startDate, number);
        }
        if("minute".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addMinutes(startDate, number);
        }
        if("hour".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addHours(startDate, number);
        }
        if("day".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addDays(startDate, number);
        }
        if("week".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addWeeks(startDate, number);
        }
        if("month".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addMonths(startDate, number);
        }
        if("year".equals(type)){
            return org.apache.commons.lang3.time.DateUtils.addYears(startDate, number);
        }
        return startDate;
    }

    /**
     * 获取两个日期相差的天数
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 天数
     */
    public static long getDays(Date beginDate, Date endDate){
        return dateToLocalDate(endDate).toEpochDay() - dateToLocalDate(beginDate).toEpochDay();
    }


    /**
     * 开始一天的开始时间
     * @param dateString 时间字符串
     * @param pattern 字符串日期格式
     * @return 一天开始的时间
     */
    public static String getOneDayStartTime(String dateString, String pattern){
        Date date = dateFromString(dateString, pattern);
        if(date == null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0);
        return  localDateTime.format(DateTimeFormatter.ofPattern(DATE_LONG));
    }


    /**
     * 开始一天的结束时间
     * @param dateString 时间字符串
     * @param pattern 字符串日期格式
     * @return 一天结束的时间
     */
    public static String getOneDayEndTime(String dateString, String pattern){
        Date date = dateFromString(dateString, pattern);
        if(date == null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        localDateTime = localDateTime.withHour(23).withMinute(59).withSecond(59);
        return  localDateTime.format(DateTimeFormatter.ofPattern(DATE_LONG));
    }
}
