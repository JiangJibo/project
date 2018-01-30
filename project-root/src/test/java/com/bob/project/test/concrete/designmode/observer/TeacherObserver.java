/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 自定义观察者
 * 
 * @since 2017年6月24日 下午3:25:51
 * @version $Id$
 * @author JiangJibo
 *
 */
public class TeacherObserver implements Observer {

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		System.out.println(((Student) o).getName() + ((Student) o).getBadThingName() + ",老师以后要严加教育");
	}

}
