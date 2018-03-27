/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.memento;

import org.junit.Test;

/**
 * 备忘录模式测试
 * 
 * @since 2017年6月23日 下午3:05:17
 * @version $Id$
 * @author JiangJibo
 *
 */
public class MementoTest {

	@Test
	public void testMemento() {
		Orginator orginator = new Orginator("abc");
		MementoIF memento = orginator.createMemento();
		CareTaker careTaker = new CareTaker();
		careTaker.saveMemento(memento);

		orginator.setState("def");
		orginator.restore(careTaker.getMemento());
		System.out.println(orginator.getState());
	}

}
