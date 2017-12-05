/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.config.mvc.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.GsonBuilder;
import org.springframework.format.Formatter;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年3月4日 下午4:06:54
 */
public class String2DateFormatter implements Formatter<Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String print(Date object, Locale locale) {
        return new GsonBuilder().setDateFormat(DATE_FORMAT).create().toJson(object);
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(text);
    }

}
