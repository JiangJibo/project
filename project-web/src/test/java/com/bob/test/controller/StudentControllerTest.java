package com.bob.test.controller;

import com.bob.config.mvc.model.CacheModel;
import com.bob.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bob.mvc.controller.RedisCacheController;
import com.google.gson.Gson;

/**
 * @since 2016年12月8日 下午5:16:52
 * @version $Id$
 * @author JiangJibo
 *
 */
public class StudentControllerTest extends BaseControllerTest {

	@Autowired
	private RedisCacheController studentController;

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {
		super.mappedController = studentController;
		super.userName = "lanboal";
		super.password = "123456";
		super.loginBefore = true;
	}

	@Test
	public void testFormatDate() {
		String result = this.getRequest("/stus/date?date=1988-07-26", "");
		System.out.println("\t" + result);
	}

	@Test
	public void testListAll() throws Exception {
		String result = this.getRequest("/stus?mediaType=xml", "");
		System.out.println("\t" + result);
	}

	@Test
	public void testListByAge() throws Exception {
		String result = this.getRequest("/stus/age/27", "");
		System.out.println("\t" + result);
	}

	@Test
	public void testFormatter() {
		String result = this.getRequest("/stus/test?student=5,jiangjibo,29,18758107777,广东&age=32", "");
		System.out.println("\t" + result);
	}

	@Test
	public void testCreate() {
		CacheModel sutdent = new CacheModel();
		sutdent.setId(10);
		sutdent.setAge(30);
		sutdent.setAdress("金华");
		sutdent.setTelephone("1748568745");
		this.postRequest(new Gson().toJson(sutdent), "/stus", "");
	}

}
