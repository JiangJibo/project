/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.root.config.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义Converter,和Formatter的区别是Converter能用在MVC的任意层,而Formatter只能用在Contoller层(将前端返回的数据解析成Model)
 * 
 * @since 2017年1月5日 下午3:26:02
 * @version $Id$
 * @author JiangJibo
 *
 */
public class String2DateConverter implements Converter<String, Date> {

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Date convert(String source) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
