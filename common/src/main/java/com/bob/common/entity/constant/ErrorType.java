package com.bob.common.entity.constant;

/**
 * 错误类型
 *
 * @author wb-jjb318191
 * @create 2018-07-24 11:18
 */
enum ErrorType {

    ARGUMENT_ERROR(1, "参数错误"),
    USER_ACCESS_ERROR(2, "用户权限错误"),
    SERVICE_ERROR(3, "业务异常");

    private int code;
    private String msg;

    ErrorType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}