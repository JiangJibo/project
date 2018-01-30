/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.template;

/**
 * @since 2017年6月22日 下午8:15:24
 * @version $Id$
 * @author JiangJibo
 *
 */
public abstract class AbstractStringProcessor {

	/**
	 * 模板方法,不可重写
	 * 
	 * @param msg
	 * @return
	 */
	public final String doHandler(String msg) {
		if (doTrimBefore()) {
			msg = msg.trim();
		}
		return "$" + process(msg);
	}

	/**
	 * 钩子方法
	 * 
	 * @return
	 */
	public boolean doTrimBefore() {
		return false;
	}

	/**
	 * 普通方法
	 * 
	 * @param msg
	 * @return
	 */
	public abstract String process(String msg);

}
