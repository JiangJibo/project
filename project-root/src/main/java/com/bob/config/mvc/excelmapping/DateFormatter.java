package com.bob.config.mvc.excelmapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间类型格式化器
 *
 * @author wb-jjb318191
 * @create 2018-01-23 13:46
 */
public class DateFormatter implements FieldFormatter<Date> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean support(Object obj) {
        return obj instanceof Date;
    }

    @Override
    public String format(Date date) {
        return DATE_FORMAT.format(date);
    }
}
