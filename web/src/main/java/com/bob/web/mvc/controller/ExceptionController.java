/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.web.mvc.controller;

import com.bob.web.config.exception.DefaultException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.bob.common.entity.constant.ErrorCodeEnum.USER_NOT_EXISTS;

/**
 * 测试ExceptionHandlerResolver
 * 
 * @since 2017年3月21日 下午7:02:28
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/exception")
public class ExceptionController {

	@RequestMapping(method = RequestMethod.GET)
	public String getDefaultExceptionMsg() {
		throw new DefaultException(USER_NOT_EXISTS);
	}

}
