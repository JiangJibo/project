package com.bob.common.utils.exception;

/**
 * 默认业务异常
 *
 * @author wb-jjb318191
 * @create 2019-09-05 14:48
 */
public class DefaultServiceException extends RuntimeException {

    private Object errorCode;

    public DefaultServiceException() {
        super();
    }

    protected DefaultServiceException(String message) {
        super(message);
    }

    protected DefaultServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected DefaultServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    protected Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }
}
