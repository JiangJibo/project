package com.bob.common.entity.result;

import java.io.Serializable;

/**
 * 类BaseResult.java的实现描述：接口返回的基础类，通过success判断本次调用在服务器端执行是否成功
 *
 * @author lixj 2015年1月26日 下午3:44:31
 */
public class BaseResult implements Serializable {

    private static final String SUCCESS_CODE = "200";
    private static final String SUCCESS_MSG = "success";
    private static final long serialVersionUID = 4398730454548225208L;

    /**
     * 标识本次调用是否执行成功
     */
    private boolean success;

    /**
     * 本次调用返回errorCode，一般为错误代码
     */
    private String errorCode;

    /**
     * 本次调用返回的消息，一般为错误消息
     */
    private String errorMsg;

    public BaseResult() {
        setErrorCode(SUCCESS_CODE);
        setErrorMsg(SUCCESS_MSG);
        this.success = true;
    }

    /**
     * 设置错误信息
     *
     * @param code
     * @param message
     */
    @SuppressWarnings("unchecked")
    public <R extends BaseResult> R setErrorMessage(String code, String message) {
        setErrorCode(code);
        setErrorMsg(message);
        this.success = false;

        return (R)this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 返回错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误码
     *
     * @param errorCode the value to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置错误消息
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
