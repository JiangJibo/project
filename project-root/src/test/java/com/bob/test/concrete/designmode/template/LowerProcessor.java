/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.designmode.template;

/**
 * @since 2017年6月22日 下午8:32:08
 * @version $Id$
 * @author JiangJibo
 *
 */
public class LowerProcessor extends AbstractStringProcessor {

	/* (non-Javadoc)
	 * @see AbstractStringProcessor#validator(java.lang.String)
	 */
	@Override
	public String process(String msg) {
		return msg.toLowerCase();
	}

}
