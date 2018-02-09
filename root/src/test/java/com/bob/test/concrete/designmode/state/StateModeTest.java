/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.state;

import org.junit.Test;

/**
 * @since 2017年6月24日 下午8:03:25
 * @version $Id$
 * @author JiangJibo
 *
 */
public class StateModeTest {

	@Test
	public void testState() {
		StateContext context = new StateContext(" Abcd", StringProcessor.LOWER);
		context.initMsg();
		context.setProcessor(StringProcessor.UPPER);
		context.initMsg();
	}

}
