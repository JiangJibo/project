/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.builder;

/**
 * @since 2017年6月24日 下午3:50:30
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Model {

	private int id;
	private String name;
	private String sex;
	private int age;
	private String adress;
	private String clazz;
	private String grade;
	private String school;

	/**
	 * @return
	 */
	public static Builder builder() {
		return new ModelBuilder();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * @param adress
	 *            the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}

	public static class ModelBuilder implements Builder {

		private Model model;

		public ModelBuilder() {
			model = new Model();
			model.setSchool("杭州第一中学");
			model.setGrade("二年级");
			model.setClazz("10班");
		}

		/* (non-Javadoc)
		 * @see Builder#setId(int)
		 */
		@Override
		public Builder setId(int id) {
			model.setId(id);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setName(java.lang.String)
		 */
		@Override
		public Builder setName(String name) {
			model.setName(name);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setSex(java.lang.String)
		 */
		@Override
		public Builder setSex(String sex) {
			model.setSex(sex);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setAge(int)
		 */
		@Override
		public Builder setAge(int age) {
			model.setAge(age);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setAdress(java.lang.String)
		 */
		@Override
		public Builder setAdress(String adress) {
			model.setAdress(adress);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setClazz(java.lang.String)
		 */
		@Override
		public Builder setClazz(String clazz) {
			model.setClazz(clazz);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setGrade(java.lang.String)
		 */
		@Override
		public Builder setGrade(String grade) {
			model.setGrade(grade);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#setSchool(java.lang.String)
		 */
		@Override
		public Builder setSchool(String school) {
			model.setSchool(school);
			return this;
		}

		/* (non-Javadoc)
		 * @see Builder#build()
		 */
		@Override
		public Model build() {
			return this.model;
		}

	}

}
