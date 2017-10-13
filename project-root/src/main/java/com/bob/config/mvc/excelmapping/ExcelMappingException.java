package com.bob.config.mvc.excelmapping;


/**
 * Excel映射异常
 *
 * @since 2017年3月21日 下午6:56:39
 * @author JiangJibo
 *
 */
public final class ExcelMappingException extends RuntimeException {
	private static final long serialVersionUID = -7261786129830851403L;

	/**
	 * Construct a <code>ExcelException</code> with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message
	 */
	public ExcelMappingException(String msg) {
		super(msg);
	}

	/**
	 * Construct a <code>ExcelException</code> with the specified detail message and nested
	 * exception.
	 * 
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the nested exception
	 */
	public ExcelMappingException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param cause
	 */
	public ExcelMappingException(Throwable cause) {
		super(cause);
	}


}
