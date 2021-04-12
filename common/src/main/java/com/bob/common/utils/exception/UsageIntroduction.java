package com.bob.common.utils.exception;

import org.springframework.core.annotation.Order;

import static com.alibaba.sec.yaxiangdi.exception.UsageIntroduction.ErrorCodeEnum.NOT_NULL;

/**
 * 异常断言使用方法介绍
 *
 * 1. 如果启动类的SimpleName是 Application, 则直接引入此jar包即可。
 *
 * 2. 如果启动类的SimpleName不是 Application, 请用{@link EnableYXDAssert} 注解标识启动类
 *
 * 3.自定义一个异常继承 {@link DefaultServiceException} , 然后定义一个枚举实现 {@link ExceptionDefinition<E extends DefaultServiceException>} 接口,
 * 同时在其泛型里指定相应的异常类型
 *
 * 4. 使用Assert里对应的断言方法, 当不匹配是会自动抛出自定义的异常, 如果有多个枚举>异常的映射, 那么用 {@link Order}来指定优先级。
 * 优先级最高的异常抛出时，可以不需要用枚举来指定类型，比如  Assert.notNull("123", "123");
 * 其他异常抛出是, 需要用枚举来指定, 比如：Assert.notNull("123", NOT_NULL, "123");
 *
 * @author wb-jjb318191
 * @create 2019-09-03 16:20
 */
public class UsageIntroduction {

    public static void main(String[] args) {
        // 当未指定枚举类型时, 如果枚举有多个,默认用@Order里值最小的异常类型
        Assert.notNull("123", "123不能为空");
        Assert.notNull("123", NOT_NULL, "123");
    }

    @Order(1)
    public enum ErrorCodeEnum implements ExceptionDefinition<YxdException> {

        NOT_NULL("00001", "[%s]不能为空");

        String code;
        String label;

        ErrorCodeEnum(String errorCode, String label) {
            this.code = errorCode;
            this.label = label;
        }

        @Override
        public Object code() {
            return code;
        }

        @Override
        public String label() {
            return label;
        }
    }

    public static class YxdException extends DefaultServiceException {

        public YxdException() {
            super();
        }

        public YxdException(String message) {
            super(message);
        }

        public YxdException(String errorCode, String message) {
            super(errorCode, message);
        }

        public YxdException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
