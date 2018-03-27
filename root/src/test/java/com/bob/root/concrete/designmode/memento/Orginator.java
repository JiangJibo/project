/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.memento;

/**
 * @since 2017年6月23日 下午2:57:29
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Orginator {

	private String state;

	/**
	 * @param state
	 */
	public Orginator(String state) {
		this.state = state;
	}

	/**
	 * 获取备忘录对象,返回标识接口的对象,不让外部获取真实类型,外部也就不能获取里面的数据
	 * 
	 * @return
	 */
	public MementoIF createMemento() {
		return new Memento(state);
	}

	/**
	 * 从备忘录里恢复数据
	 * 
	 * @param memento
	 */
	public void restore(MementoIF memento) {
		this.state = ((Memento) memento).getSaveState();
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 实现一个标识接口
	 * 
	 * @since 2017年6月23日 下午3:09:06
	 * @version $Id$
	 * @author JiangJibo
	 *
	 */
	protected class Memento implements MementoIF {

		private String saveState;

		private Memento(String saveState) {
			this.saveState = saveState;
		}

		/**
		 * @return the saveState
		 */
		private String getSaveState() {
			return saveState;
		}

	}

}
