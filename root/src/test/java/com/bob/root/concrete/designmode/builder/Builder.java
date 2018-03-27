/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.builder;

/**
 * 建造者接口,很多情况可以略去
 * 
 * @since 2017年6月24日 下午3:50:06
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface Builder {

	public Builder setId(int id);

	public Builder setName(String name);

	public Builder setSex(String sex);

	public Builder setAge(int age);

	public Builder setAdress(String adress);

	public Builder setClazz(String clazz);

	public Builder setGrade(String grade);

	public Builder setSchool(String school);

	/**
	 * 获取最终要创造的对象
	 * 
	 * @return
	 */
	public Model build();
}
