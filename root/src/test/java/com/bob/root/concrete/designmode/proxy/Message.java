/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.concrete.designmode.proxy;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @since 2017年5月31日 上午9:55:24
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 8003425788006614100L;

	private int id;
	private String text;
	private LocalDateTime dateTime;

	/**
	 * @param id
	 * @param text
	 * @param dateTime
	 */
	public Message(int id, String text, LocalDateTime dateTime) {
		super();
		this.id = id;
		this.text = text;
		this.dateTime = dateTime;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the dateTime
	 */
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            the dateTime to set
	 */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}
