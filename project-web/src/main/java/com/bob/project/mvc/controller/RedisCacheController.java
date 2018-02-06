/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.mvc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bob.project.mvc.service.CacheModelService;
import com.bob.project.web.config.formatter.String2DateFormatter;
import com.bob.project.web.config.model.CacheModel;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @since 2016年12月6日 上午11:29:41
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/stus")
public class RedisCacheController {

	final static Logger LOGGER = LoggerFactory.getLogger(RedisCacheController.class);

	@Autowired
	private CacheModelService cacheModelService;

	@InitBinder
	public void registarPropertyEditor(WebDataBinder binder) {
		binder.addCustomFormatter(new String2DateFormatter(), Date.class);
		// binder.setAllowedFields("id", "name", "telephone");
		System.out.println(binder.isIgnoreInvalidFields());
	}

	@RequestMapping(value = "/date", method = RequestMethod.GET)
	public String formatDate(Date date) {
		System.out.println(date.toString());
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	// @Cacheable(value = "students")
	@RequestMapping(method = RequestMethod.GET)
	public List<CacheModel> listAll() {
		LOGGER.debug("缓存students!");
		return cacheModelService.listAll();
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String testFormatter(@Validated CacheModel student, BindingResult result) {
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				System.out.println(error.getField());
				System.out.println(error.getCode());
			}
		}
		String msg = BeanUtils.instantiate(Gson.class).toJson(student);
		System.out.println(msg);
		return msg;
	}

	// @Cacheable(value = "students", condition = "#age > 27")
	@RequestMapping(value = "/age/{age}", method = RequestMethod.GET)
	public List<CacheModel> listByAge(@PathVariable Integer age) {
		return cacheModelService.listByAge(age);
	}

	@Cacheable(value = "student", key = "#id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CacheModel get(@PathVariable Integer id) {
		LOGGER.error("执行查询id=" + id + "的学生");
		return cacheModelService.getById(id);
	}

	@CachePut(value = "student", key = "#id")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public CacheModel putStudent(@PathVariable Integer id, @RequestBody CacheModel student) {
		Integer updatedRow = cacheModelService.updateById(student);
		LOGGER.error("执行更新id=" + id + "的学生");
		if (updatedRow == 0) {
			throw new IllegalStateException("更新id为:" + id + "的学生失败");
		}
		return cacheModelService.getById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(HttpServletRequest request, @RequestBody CacheModel stu) throws IOException {
		System.out.println(request.getContentType());
		InputStream is = request.getInputStream();
		List<String> lines = IOUtils.readLines(is);
		for (String string : lines) {
			System.out.println(string);
		}
		// studentService.create(stu);
		return null;
	}

}
