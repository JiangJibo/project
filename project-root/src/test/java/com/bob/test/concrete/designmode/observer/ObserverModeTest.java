/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.observer;

import org.junit.Test;

/**
 * 观察者模式测试用例
 * 
 * @since 2017年6月24日 下午3:34:47
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ObserverModeTest {

	@Test
	public void testObserver() {
		Student stu = new Student();
		stu.addObserver(new ParentObserver());
		stu.addObserver(new TeacherObserver());
		stu.addObserver(new SchoolObserver());
		stu.doBadThings("殴打同学");
	}

}
