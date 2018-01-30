package com.bob.project.config.mvc.excelmapping;

import java.io.Serializable;

/**
 * Excel成功解析返回对象,按行存储
 *
 * @since 2017年3月21日 下午6:56:39
 * @author JiangJibo
 *
 */
public class ExcelInstance<T> implements Serializable {
	private static final long serialVersionUID = 4183046707691570188L;

	private int rowIndex;
	private T instance;

	public ExcelInstance(int rowIndex, T instance) {
		super();
		this.rowIndex = rowIndex;
		this.instance = instance;
	}

	/**
	 * 行号
	 * 
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex
	 *            the rowIndex to set
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * 解析成功对象信息
	 * 
	 * @return the instance
	 */
	public T getInstance() {
		return instance;
	}

	/**
	 * @param instance
	 *            the instance to set
	 */
	public void setInstance(T instance) {
		this.instance = instance;
	}
}
