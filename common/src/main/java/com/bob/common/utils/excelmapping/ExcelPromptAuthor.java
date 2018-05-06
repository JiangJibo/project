package com.bob.common.utils.excelmapping;

/**
 * @since 2016年6月3日 上午11:32:23
 * @author HuMing
 *
 */
public enum ExcelPromptAuthor {

	JJB("jjb");

	private String author;

	/**
	 * @param author
	 */
	private ExcelPromptAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
}
