package com.bob.common.utils.rocket.util;

import com.bob.common.utils.rocket.listener.AbstractMessageListener;

import static com.bob.common.utils.rocket.processor.RocketListenerAnnotationBeanPostProcessor.LISTENER_MAX_RECONSUME_TIMES_MAPPINGS;

/**
 * 工具类
 *
 * @author wb-jjb318191
 * @create 2018-04-08 15:21
 */
public class RocketUtils {

    /**
     * 获取指定消费者的最大消费次数
     * 当未显式指定时,默认值为16
     *
     * @param messageListener
     * @return
     */
    public static int getMaxReconsumeTimes(AbstractMessageListener messageListener) {
        int maxReconsumeTimes = LISTENER_MAX_RECONSUME_TIMES_MAPPINGS.get(messageListener);
        return maxReconsumeTimes == -1 ? 16 : maxReconsumeTimes;
    }

}
