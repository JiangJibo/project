package com.bob.project.web.config.controlleradvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.bob.project.web.config.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * @since 2017年7月29日 上午10:33:23
 * @version $Id$
 * @author JiangJibo
 *
 */
// @ControllerAdvice(assignableTypes = { UserController.class })
public class CustomizedRequestBodyAdvice extends RequestBodyAdviceAdapter {

	final static Logger LOGGER = LoggerFactory.getLogger(CustomizedRequestBodyAdvice.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.com.bob.config.mvc.method.annotation.RequestBodyAdvice#supports(org.springframework.core.MethodParameter, java.lang.reflect.Type, java.lang.Class)
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return User.class.getName().equals(targetType.getTypeName());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.com.bob.config.mvc.method.annotation.RequestBodyAdviceAdapter#beforeBodyRead(org.springframework.http.HttpInputMessage, org.springframework.core.MethodParameter, java.lang.reflect.Type, java.lang.Class)
	 */
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		InputStream is = inputMessage.getBody();
		byte[] bytes = new byte[1024];
		is.read(bytes);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("请求的请求体为[{}]", new String(bytes, Charset.forName("UTF-8")));
		}
		// TODO,inputMessage读取的IO流不支持mark,读取完之后pos无法复原,@RequestBody解析将失败
		return inputMessage;
	}

}
