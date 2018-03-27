/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.template;

/**
 * @since 2017年6月22日 下午8:30:56
 * @version $Id$
 * @author JiangJibo
 *
 */
public class UpperProcessor extends AbstractStringProcessor {

	/* (non-Javadoc)
	 * @see AbstractStringProcessor#doTrimBefore()
	 */
	@Override
	public boolean doTrimBefore() {
		return true;
	}

	/* (non-Javadoc)
	 * @see AbstractStringProcessor#validator(java.lang.String)
	 */
	@Override
	public String process(String msg) {
		return msg.toUpperCase();
	}

}
