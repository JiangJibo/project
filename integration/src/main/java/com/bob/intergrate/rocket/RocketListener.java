package com.bob.intergrate.rocket;

/**
 * RocketMQ消费者监听
 *
 * @author wb-jjb318191
 * @create 2018-03-20 9:19
 */
public @interface RocketListener {

    /**
     * 消费者组
     *
     * @return
     */
    String group();

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
    String namesrv() default "";

}