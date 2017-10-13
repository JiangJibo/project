/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.root.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.bob.config.mvc.model.CacheModel;

/**
 * FactoryBean的使用方法
 * 
 * @since 2017年1月16日 上午10:20:11
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class StudentFactory implements FactoryBean<CacheModel> {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public CacheModel getObject() throws Exception {
		CacheModel student = new CacheModel();
		student.setId(001);
		student.setName("lanboal");
		student.setAge(28);
		student.setAdress("杭州");
		student.setTelephone("18758107760");
		return student;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return CacheModel.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
