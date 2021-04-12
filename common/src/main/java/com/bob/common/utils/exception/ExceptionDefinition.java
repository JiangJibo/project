package com.bob.common.utils.exception;

/**
 * 异常类型定义
 *
 * @param <E> 异常类型
 * @author wb-jjb318191
 */
public interface ExceptionDefinition<E extends DefaultServiceException> {

    /**
     * 错误编码
     *
     * @return
     */
    Object code();

    /**
     * 错误提示信息
     *
     * @return
     */
    String label();

}