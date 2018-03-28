package com.bob.web.config.exception;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bob.common.entity.constant.ErrorCodeEnum;
import com.bob.common.entity.result.BaseResult;
import com.bob.web.config.model.CacheModel;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义异常解析器
 *
 * @since 2017年3月21日 下午6:55:12
 * @version $Id$
 * @author JiangJibo
 *
 */
public class DefaultExceptionResolver implements HandlerExceptionResolver {


    private static final Gson GSON = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ErrorCodeEnum error;
        BaseResult result = new BaseResult();
        if (ex instanceof DefaultException) {
            error = ((DefaultException)ex).getErrorCodeEnum();
        } else {
            error = ErrorCodeEnum.valueOf(ex.getLocalizedMessage());
        }
        if (error == null) {
            result.setErrorMessage("", ex.getLocalizedMessage());
        } else {
            result.setErrorMessage(error.getCode(), error.getMsg());
        }
        LOGGER.error(result.getErrorMsg(), ex);
        try {
            response.setContentType("UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(GSON.toJson(result).getBytes(Charset.forName("UTF-8")));
            return new ModelAndView();
        } catch (IOException e1) {
            LOGGER.error(e1.getLocalizedMessage(), e1);
            return null;
        }
    }

}
