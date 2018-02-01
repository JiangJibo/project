package com.bob.project.utils.excelmapping.exception;

/**
 * Excel异常
 *
 * @author wb-jjb318191
 * @create 2017-09-19 10:18
 */
public class ExcelException extends RuntimeException {

    public ExcelException() {
        super();
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }
}
