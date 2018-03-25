package com.bob.intergrate.rocket.integrate.constant;

/**
 * RocketMQ定义时的常量
 *
 * @author wb-jjb318191
 * @create 2018-03-20 14:38
 */
public interface RocketBeanDefinitionConstant {

    /**
     * 消费者组
     */
    String CONSUMER_GROUP = "consumerGroup";

    /**
     * 主题
     */
    String TOPIC = "topic";

    /**
     * 小标题
     */
    String TAG = "tag";

    /**
     *
     */
    String NAMESRV_ADDR = "namesrvAddr";

    /**
     * 消费Bean
     */
    String CONSUME_BEAN_NAME = "consumeBeanName";

    /**
     * 消费方法
     */
    String CONSUME_METHOD = "consumeMethod";

    /**
     * 消费偏移模式
     */
    String CONSUME_FROM_WHERE = "consumeFromWhere";

    /**
     * RocketMQ消费者Bean名称后缀
     */
    String ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX = "RocketConsumer";

    /**
     * 是否有序消费
     */
    String ORDERLY = "orderly";

}