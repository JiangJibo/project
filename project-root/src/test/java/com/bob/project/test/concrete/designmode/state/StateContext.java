/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.test.concrete.designmode.state;

import org.springframework.util.Assert;

/**
 * @since 2017年6月24日 下午7:46:34
 * @version $Id$
 * @author JiangJibo
 *
 */
public class StateContext {

	private String msg;

	private StringProcessor processor;

	public StateContext() {
	}

	/**
	 * @param msg
	 * @param processor
	 */
	public StateContext(String msg, StringProcessor processor) {
		this.msg = msg;
		this.processor = processor;
	}

	public void initMsg() {
		Assert.notNull(processor, "processor不能为null");
		String temp = processor.doProcess(msg);
		System.out.println(processor.getLabel() + "将" + msg + "处理成" + temp);
		this.msg = temp;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the processor
	 */
	public StringProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor
	 *            the processor to set
	 */
	public void setProcessor(StringProcessor processor) {
		this.processor = processor;
	}

}
