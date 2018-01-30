/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.proxy;

/**
 * @since 2017年5月31日 上午9:54:51
 * @version $Id$
 * @author JiangJibo
 *
 */
public class RealSubject implements Subject, Handler {

	/* (non-Javadoc)
	 * @see com.bob.test.concrete.proxy.Handler#handler()
	 */
	@Override
	public void print() {
		System.out.println("今天的天气是晴天");
	}

	/* (non-Javadoc)
	 * @see com.bob.test.concrete.proxy.Handler#print()
	 */
	@Override
	public void handler(Message msg) {
		System.out.println("id:" + msg.getId() + ",text:" + msg.getText() + ",time:" + msg.getDateTime().toString());
	}

}
