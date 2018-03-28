/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.web.config.exception;

import com.bob.common.entity.constant.ErrorCodeEnum;

/**
 * 自定义异常,在合适的场合可以抛出
 *
 * @since 2017年3月21日 下午6:56:39
 * @version $Id$
 * @author JiangJibo
 *
 */
public class DefaultException extends RuntimeException {

    private static final long serialVersionUID = -6372945904146943934L;

    private ErrorCodeEnum errorCodeEnum;

    public DefaultException(String message) {
        super(message);
    }

    public DefaultException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public DefaultException(ErrorCodeEnum errorCodeEnum, Throwable cause) {
        super(cause);
        this.errorCodeEnum = errorCodeEnum;
    }

    public ErrorCodeEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }

}
