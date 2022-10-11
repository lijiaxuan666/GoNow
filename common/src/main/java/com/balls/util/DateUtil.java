package com.balls.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {

    //精确到秒的格式
    private SimpleDateFormat formatSec =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //精确到分的格式
    private SimpleDateFormat formatMin=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /** 返回精确到秒的字符串格式时间
     *
     * @return String类型日期
     */
    public String getNowTimeAsString(){
        return formatSec.format(new Date());
    }

    /** 返回date类型日期
     *
     * @return Date类型日期
     */
    public Date getNowTime(){
        return new Date();
    }

    /**  返回字符串日期到1970年1月1日的毫秒值
     *
     * @param time 精确到分钟的日期 yyyy-MM-dd HH:mm
     * @return long类型毫秒值
     * @throws ParseException
     */
    public Long getTime(String time) throws ParseException {
        Date date=formatMin.parse(time);
        return date.getTime();
    }

    public Long getTime(){
        return new Date().getTime();
    }

}
