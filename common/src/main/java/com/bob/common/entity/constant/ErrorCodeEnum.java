package com.bob.common.entity.constant;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import static com.bob.common.entity.constant.ErrorType.ARGUMENT_ERROR;
import static com.bob.common.entity.constant.ErrorType.USER_ACCESS_ERROR;

/**
 * 错误枚举
 * 应用内打印{@link ErrorCodeEnum#getMsg()}
 * 封装错误信息,仅对用户展示{@link ErrorType#getMsg()}
 *
 * @author wb-jjb318191
 * @create 2018-03-28 9:31
 */
public enum ErrorCodeEnum {

    USER_NOT_EXISTS("401", "操作用户[%s]不存在", USER_ACCESS_ERROR),
    USER_ID_IS_NULL("402", "用户id:[%s]不能为空", ARGUMENT_ERROR);

    private String code;
    private String msg;
    private ErrorType type;

    ErrorCodeEnum(String code, String msg, ErrorType type) {
        this.code = code;
        this.msg = msg;
        this.type = type;
    }

    private static final Map<String, ErrorCodeEnum> values = new HashMap<>();

    static {
        for (ErrorCodeEnum en : ErrorCodeEnum.values()) {
            values.put(en.getMsg(), en);
        }
    }

    /**
     * 构建异常信息
     *
     * @param args
     * @return
     */
    public String buildErrorMsg(Object... args) {
        return String.format(getMsg(), args);
    }

    /**
     * 寻找对对应的错误类型枚举
     *
     * @param msg
     * @return
     */
    public static ErrorType introspectErrorType(String msg) {
        Assert.hasText(msg, "异常信息不能为空");
        StringBuilder sb = new StringBuilder();
        boolean match = true;
        for (char c : msg.toCharArray()) {
            switch (c) {
                case '[':
                    Assert.isTrue(match, "错误信息格式不正确");
                    match = false;
                    sb.append("[%s");
                    break;
                case ']':
                    sb.append("]");
                    match = true;
                    break;
                default:
                    if (match) {
                        sb.append(c);
                    }
            }
        }
        if (!match) {
            throw new IllegalArgumentException("错误信息格式不正确");
        }
        String rawMsg = sb.toString();
        ErrorCodeEnum en = values.get(rawMsg);
        Assert.notNull(en, String.format("错误信息:{%s}没有对应的错误枚举", msg));
        return en.type;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ErrorType getType() {
        return type;
    }

}