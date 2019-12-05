package com.bob.common.utils.excelmapping;

/**
 * @author
 * @since 2016年6月3日 上午11:32:23
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
