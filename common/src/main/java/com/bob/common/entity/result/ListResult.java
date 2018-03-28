package com.bob.common.entity.result;



import java.util.List;

/**
 * @param <T>
 */
public class ListResult<T> extends BaseResult {

    private static final long serialVersionUID = 4523024906338896795L;
    private List<T> content;
    private Integer totalCount;

    public ListResult() {
    }

    public ListResult(List<T> content) {
        this.content = content;
        if (content != null) {
            this.totalCount = content.size();
        }
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getContent() {
        return this.content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
