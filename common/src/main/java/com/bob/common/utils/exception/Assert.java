package com.bob.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言
 *
 * @author wb-jjb318191
 * @create 2019-09-02 10:28
 */
public abstract class Assert {

    private static final Logger LOGGER = LoggerFactory.getLogger(Assert.class);

    /**
     * 判断是否匹配
     *
     * @param arg1
     * @param arg2
     * @param message
     */
    public static void equals(Object arg1, Object arg2, String message) {
        if (!arg1.equals(arg2)) {
            throwsException(message);
        }
    }

    /**
     * 判断是否匹配
     *
     * @param arg1
     * @param arg2
     * @param exceptionDefinition
     * @param args                是否带拼接参数
     */
    public static void equals(Object arg1, Object arg2, ExceptionDefinition exceptionDefinition, Object... args) {
        if (!arg1.equals(arg2)) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * 判断成功与否
     *
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throwsException(message);
        }
    }

    /**
     * 判断成功与否
     *
     * @param expression
     * @param exceptionDefinition
     * @param args                是否带拼接参数
     */
    public static void isTrue(boolean expression, ExceptionDefinition exceptionDefinition, Object... args) {
        if (!expression) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * Map集合不为空
     *
     * @param map
     * @param message
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throwsException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, ExceptionDefinition exceptionDefinition, Object... args) {
        if (CollectionUtils.isEmpty(map)) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * Collection集合不为空
     *
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throwsException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, ExceptionDefinition exceptionDefinition, Object... args) {
        if (CollectionUtils.isEmpty(collection)) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * 字符串不为空
     *
     * @param text
     * @param message
     */
    public static void notEmpty(String text, String message) {
        if (text == null || text.trim().length() == 0) {
            throwsException(message);
        }
    }

    public static void notEmpty(String text, ExceptionDefinition exceptionDefinition, Object... args) {
        if (text == null || text.trim().length() == 0) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * 对象不为null
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throwsException(message);
        }
    }

    public static void notNull(Object object, ExceptionDefinition exceptionDefinition, Object... args) {
        if (object == null) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * 对象为null
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throwsException(message);
        }
    }

    public static void isNull(Object object, ExceptionDefinition exceptionDefinition, Object... args) {
        if (object != null) {
            throwsException(exceptionDefinition, args);
        }
    }

    /**
     * 抛出异常
     *
     * @param message
     */
    private static void throwsException(String message) {
        DefaultServiceException exception = ExceptionDefinitionHandler.newException(message);
        LOGGER.error("Throws {}, errorInfo:[{}]", exception.getClass().getSimpleName(), message);
        throw exception;
    }

    /**
     * 抛出异常
     *
     * @param exceptionDefinition
     * @param args
     */
    private static void throwsException(ExceptionDefinition exceptionDefinition, Object... args) {
        String errorMsg = exceptionDefinition.label();
        if (args != null && args.length > 0) {
            errorMsg = String.format(errorMsg, args);
        }
        DefaultServiceException exception = ExceptionDefinitionHandler.newException(exceptionDefinition,
            exceptionDefinition.code(), errorMsg);
        LOGGER.error("Throws {}, code:[{}], errorInfo:[{}]", exception.getClass().getSimpleName(),
            exceptionDefinition.code(), errorMsg);
        throw exception;
    }

}
