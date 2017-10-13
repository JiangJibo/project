/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * @since 2017年3月4日 下午4:06:54
 * @version $Id$
 * @author JiangJibo
 *
 */
public class String2DateFormatter implements Formatter<Date> {

	/* (non-Javadoc)
	 * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String print(Date object, Locale locale) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	 */
	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(text);
	}

}
