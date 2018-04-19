package com.bob.common.utils.rocket.util;

import java.util.HashMap;
import java.util.Map;

import com.bob.common.utils.rocket.listener.AbstractMessageListener;

/**
 * 工具类
 *
 * @author wb-jjb318191
 * @create 2018-04-08 15:21
 */
public class RocketUtils {

    /**
     * 消息监听器 >> 最大消费次数 的映射关系
     */
    public static final Map<AbstractMessageListener, Integer> LISTENER_MAX_RECONSUME_TIMES_MAPPINGS = new HashMap<>();

    /**
     * 添加最大消费次数映射,当未显式指定时,默认值为16
     *
     * @param messageListener
     * @param reconsumeTimes
     */
    public static void addListener2MaxReconsumeMappings(AbstractMessageListener messageListener, int reconsumeTimes) {
        LISTENER_MAX_RECONSUME_TIMES_MAPPINGS.put(messageListener, reconsumeTimes == -1 ? 16 : reconsumeTimes);
    }

    /**
     * 获取指定消费者的最大消费次数
     *
     * @param messageListener
     * @return
     */
    public static int getMaxReconsumeTimes(AbstractMessageListener messageListener) {
        return LISTENER_MAX_RECONSUME_TIMES_MAPPINGS.get(messageListener);
    }

}
