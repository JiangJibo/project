package com.bob.root.utils;

import java.io.Serializable;

/**
 * 基础Model
 *
 * @author wb-jjb318191
 * @create 2018-01-16 13:16
 */
public class BaseModel implements Serializable {

    private static final long serialVersionUID = -7192344879797520674L;

    /**
     * 当前页，初始值为1
     */
    private Integer currentPage = 1;

    /**
     * 起始记录数，初始值为1
     */
    private Integer startRow = 1;

    /**
     * 页大小，初始值为10
     */
    private Integer limit = 10;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getStartRow() {
        if (currentPage != null && currentPage > 0) {
            startRow = (currentPage - 1) * limit;
        }
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
