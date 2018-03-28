package com.bob.common.entity.constant;

/**
 * 错误枚举
 *
 * @author wb-jjb318191
 * @create 2018-03-28 9:31
 */
public enum ErrorCodeEnum {

    USER_NOT_EXISTS("401", "操作用户不存在"),
    USER_ID_IS_NULL("402", "用户id不能为空");

    private String code;
    private String msg;

    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}