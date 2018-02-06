/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.formatter;

import java.text.ParseException;
import java.util.Locale;

import com.bob.project.config.model.CacheModel;
import org.springframework.beans.BeanUtils;
import org.springframework.format.Formatter;
import org.springframework.util.Assert;


import com.google.gson.Gson;

/**
 * 针对前端返回的数据(String)解析成自己想要的Model
 * 
 * @since 2017年3月2日 下午1:55:32
 * @version $Id$
 * @author JiangJibo
 *
 */
public class StudentFormatter implements Formatter<CacheModel> {

	/* (non-Javadoc)
	 * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String print(CacheModel object, Locale locale) {
		return BeanUtils.instantiate(Gson.class).toJson(object);
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	 */
	@Override
	public CacheModel parse(String text, Locale locale) throws ParseException {
		Assert.hasText(text, "Formatter在解析Student信息时出现错误,数据不能为null或''");
		String[] fields = text.split(",");
		CacheModel stu = new CacheModel();
		stu.setId(Integer.valueOf(fields[0]));
		stu.setName(fields[1]);
		stu.setAge(Integer.valueOf(fields[2]));
		stu.setTelephone(fields[3]);
		stu.setAdress(fields[4]);
		return stu;
	}

}
