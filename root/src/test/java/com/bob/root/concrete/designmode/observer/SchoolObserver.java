/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 学校观察者
 * 
 * @since 2017年6月24日 下午3:27:08
 * @version $Id$
 * @author JiangJibo
 *
 */
public class SchoolObserver implements Observer {

	/* (non-Javadoc)
	 * @see java.process.Observer#update(java.process.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		System.out.println(((Student) o).getName() + ((Student) o).getBadThingName() + ",记过处分");
	}

}
