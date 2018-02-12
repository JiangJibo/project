/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.builder;

import org.junit.After;
import org.junit.Test;

import com.google.gson.Gson;

/**
 * 建造者模式测试用例
 * 
 * @since 2017年6月24日 下午4:06:33
 * @version $Id$
 * @author JiangJibo
 *
 */
public class BuilderModeTest {

	private Model model;

	@After
	public void doAfterProcessing() {
		System.out.println(new Gson().toJson(model));
	}

	@Test
	public void testBuilderModel() {
		model = Model.builder().setId(100).setAdress("杭州").setAge(29).setClazz("2班").setGrade("一年级").build();
	}

	@Test
	public void testNewModel() {
		model = new Model();
		model.setId(100);
		model.setAdress("杭州");
		model.setAge(29);
		model.setClazz("2班");
		model.setGrade("一年级");
	}

}
