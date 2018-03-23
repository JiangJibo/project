package com.bob.test.concrete.rocket;

import java.net.UnknownHostException;

import com.google.gson.Gson;
import org.apache.rocketmq.common.message.MessageDecoder;
import org.apache.rocketmq.common.message.MessageId;
import org.junit.Test;

/**
 * @author Administrator
 * @create 2018-03-22 20:17
 */
public class RocketmqUtilTest {

    private static final Gson GSON = new Gson();

    /**
     * @throws UnknownHostException
     */
    @Test
    public void decodeMessageId() throws UnknownHostException {
        String msgId = "C0A80B6600002A9F00000000000087C7";
        MessageId messageId = MessageDecoder.decodeMessageId(msgId);
        System.out.println(GSON.toJson(messageId));
    }

}
