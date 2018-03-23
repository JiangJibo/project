package com.bob.intergrate.rocket.integrate.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import static org.apache.rocketmq.common.consumer.ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;

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
    String consumerGroup();

    /**
     * @return
     */
    String topic();

    /**
     * @return
     */
    String tag() default "*";

    /**
     * @return
     */
    String namesrvAddr() default "";

    /**
     * 从哪里开始消费
     *
     * @return
     */
    ConsumeFromWhere consumeFromWhere() default CONSUME_FROM_LAST_OFFSET;

}