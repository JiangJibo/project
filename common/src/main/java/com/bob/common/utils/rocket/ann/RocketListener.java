package com.bob.common.utils.rocket.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bob.common.utils.rocket.handler.ConsumeFailureHandler;
import com.bob.common.utils.rocket.handler.ConsumeFailureHandlerAdapter;

/**
 * RocketMQ消费者监听
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RocketListener {

    /**
     * 消费者组
     *
     * @return
     */
    String consumerGroup() default "";

    /**
     * @return
     */
    String topic() default "";

    /**
     * @return
     */
    String tag() default "";

    /**
     * @return
     */
    String namesrvAddr() default "";

    /**
     * 是否有序
     *
     * @return
     */
    boolean orderly() default false;

    /**
     * 配置Properties文件名称
     *
     * @return
     */
    String configProperties() default "";

    /**
     * 消费失败处理器 BeanName
     *
     * @return
     */
    String failureHandler() default "";

}