package com.bob.common.utils.userenv.ex;

/**
 * 用户属性注入异常
 *
 * @author Administrator
 * @create 2018-03-27 19:09
 */
public class UserEnvInjectingException extends RuntimeException {

    public UserEnvInjectingException() {
        super();
    }

    public UserEnvInjectingException(String message) {
        super(message);
    }

    public UserEnvInjectingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEnvInjectingException(Throwable cause) {
        super(cause);
    }
}
