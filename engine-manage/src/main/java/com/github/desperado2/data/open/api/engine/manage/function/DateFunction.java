package com.github.desperado2.data.open.api.engine.manage.function;


import com.github.desperado2.data.open.api.common.manage.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @author tu nan
 * @date 2023/5/18
 **/
@Component
public class DateFunction implements IFunction{

    @Override
    public String getVarName() {
        return "DateUtils";
    }


    public Date getDate(String dateStr, String pattern){
        return DateUtils.dateFromString(dateStr, pattern);
    }

    public String getDateFormat(Date dateStr, String pattern){
        return DateUtils.dateFormat(dateStr, pattern);
    }

    public List<String> getDayList(Date startDate, Date endDate){
        return DateUtils.getDayRange(startDate, endDate, DateUtils.DATE_SHORT);
    }

    public List<String> getWeekList(Date startDate, Date endDate){
        return DateUtils.getDayRange(startDate, endDate, DateUtils.DATE_SHORT);
    }

    public List<String> getMonthList(Date startDate, Date endDate){
        return DateUtils.getDayRange(startDate, endDate, DateUtils.MONTH_SHORT);
    }

    public Date getNewDate(Date startDate, Integer number, String type){
        return DateUtils.getNewDate(startDate, number, type);
    }

    public List<String> getOneDayAllHours(){
        return DateUtils.getOneDayAllHours();
    }
}
