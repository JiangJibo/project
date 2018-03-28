package com.bob.common.entity.result;

/**
 * 类PojoResult.java的实现描述：返回结果带有一个pojo对象
 *
 * @param <T>
 */
public class PojoResult<T> extends BaseResult {

    private static final long serialVersionUID = -7974430058913590877L;

    private T content;

    public PojoResult() {
    }

    public PojoResult(T content) {
        this.content = content;
    }

    /**
     * @return the content
     */
    public T getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(T content) {
        this.content = content;
    }

}
