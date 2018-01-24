package com.bob.config.mvc.excelmapping.transform;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间类型格式化器
 *
 * @author wb-jjb318191
 * @create 2018-01-23 13:46
 */
public class DateFormatter implements FieldFormatter<Date, String> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean support(Field field, Object value) {
        return value instanceof Date;
    }

    @Override
    public String format(Date date) {
        return date != null ? DATE_FORMAT.format(date) : null;
    }
}
