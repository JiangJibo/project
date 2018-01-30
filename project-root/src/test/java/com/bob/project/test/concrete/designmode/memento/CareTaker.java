/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.memento;

/**
 * 储存备忘录对象的容器
 * 
 * @since 2017年6月23日 下午3:04:29
 * @version $Id$
 * @author JiangJibo
 *
 */
public class CareTaker {

	private MementoIF memento;

	/**
	 * @return the memento
	 */
	public MementoIF getMemento() {
		return memento;
	}

	/**
	 * @param memento
	 *            the memento to set
	 */
	public void saveMemento(MementoIF memento) {
		this.memento = memento;
	}

}
