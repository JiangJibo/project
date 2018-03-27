/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.template;

import org.junit.Test;

/**
 * 模板方法模式测试
 * 
 * @since 2017年6月22日 下午8:32:50
 * @version $Id$
 * @author JiangJibo
 *
 */
public class TemplateModeTest {

	@Test
	public void testTemplate() {
		String msg = " abCdeFG";
		AbstractStringProcessor processor = new UpperProcessor();
		System.out.println(processor.doHandler(msg));
		processor = new LowerProcessor();
		System.out.println(processor.doHandler(msg));
	}

}
