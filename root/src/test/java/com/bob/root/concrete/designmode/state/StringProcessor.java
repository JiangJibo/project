/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.state;

import org.springframework.util.Assert;

/**
 * 状态模式的状态集
 * 
 * @since 2017年6月24日 下午7:27:46
 * @version $Id$
 * @author JiangJibo
 *
 */
public enum StringProcessor {

	UPPER("upper") {

		public String doProcess(String msg) {
			return msg.toUpperCase();
		}
	},

	LOWER("lower") {

		public String doProcess(String msg) {
			return msg.toLowerCase();
		}

	};

	private String label;

	private StringProcessor(String label) {
		this.label = label;
	}

	public final String process(String msg) {
		Assert.hasLength(msg, "待处理字符串必须非空");
		msg = doTrimBefore(msg);
		return doProcess(msg);
	}

	abstract String doProcess(String msg);

	private String doTrimBefore(String msg) {
		return msg.trim();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
