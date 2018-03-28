package com.bob.common.entity.result;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 8948211772614647628L;

    private List<T> data;
    private int total;
    private int totalPage;
    private int limit;
    private int currentPage;

    private void initTotalPage() {
        this.totalPage = total % limit == 0 ? (total / limit) : (total / limit) + 1;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setTotal(int total) {
        this.total = total;
        if (limit > 0) {
            initTotalPage();
        }
    }

    public void setLimit(int limit) {
        this.limit = limit;
        if (total > 0) {
            initTotalPage();
        }
    }

    public List<T> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getLimit() {
        return limit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
