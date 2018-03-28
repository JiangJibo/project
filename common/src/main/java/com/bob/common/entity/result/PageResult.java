package com.bob.common.entity.result;

/**
 * 类CollectionResult.java的实现描述：分页返回结果
 *
 * @param <T>
 */
public class PageResult<T> extends BaseResult {

    private static final long serialVersionUID = 4523024906338896795L;

    private Page<T> content;

    public PageResult() {
    }

    public PageResult(Page<T> content) {
        this.content = content;
    }

    /**
     * @return the content
     */
    public Page<T> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Page<T> content) {
        this.content = content;
    }

}
