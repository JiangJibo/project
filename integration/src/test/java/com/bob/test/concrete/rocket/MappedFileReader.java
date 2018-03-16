package com.bob.test.concrete.rocket;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;

import com.google.gson.Gson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MappedFile信息读取
 *
 * @author wb-jjb318191
 * @create 2018-03-16 14:20
 */
public class MappedFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MappedFileReader.class);

    private String filePath;
    private MappedByteBuffer mappedByteBuffer;

    @Test
    public void readConsumeQueue() throws IOException {
        filePath = "C:\\Users\\wb-jjb318191\\store\\consumequeue\\SCHEDULE_TOPIC_XXXX\\2\\00000000000000000000";
        init();
        long offset = mappedByteBuffer.getLong();  //193774
        LOGGER.debug("当前消息在CommitLog的位置是第[{}]字节开始", offset);
        int size = mappedByteBuffer.getInt();  //336
        LOGGER.debug("当前消息总大小[{}]字节", size);
        long tagCode = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的Tag的HashCode是[{}]", tagCode);
    }

    @Test
    public void testReadCommitLog() throws IOException {
        filePath = "C:\\Users\\wb-jjb318191\\store\\commitlog\\00000000000000000000";
        init();
        readCommitLogByPosition(193435);
    }

    /**
     * 从CommitLog指定位置开始读取消息
     *
     * @param position
     */
    private void readCommitLogByPosition(long position) {
        mappedByteBuffer.position(193435);
        int size = mappedByteBuffer.getInt();  //336
        LOGGER.debug("当前消息总大小[{}]字节", size);
        int magicCode = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的MagicCode:[{}]", magicCode);
        int bodyRPC = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的BodyRPC:[{}]", bodyRPC);
        int queueId = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的QueueId:[{}]", queueId);
        int flag = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的Flag:[{}]", flag);
        long queueOffset = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的ConsumeQueue Offset:[{}]", queueOffset);
        long physicalOffset = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的CommitLog Offset:[{}]", physicalOffset);
        int sysFlag = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的SysFlag:[{}]", sysFlag);
        long bornTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的BornTimestamp:[{}]", new Gson().toJson(new Date(bornTimestamp)));
        long bornHost = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的BornHost:[{}]", bornHost);
        long storeTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的BornTimestamp:[{}]", new Gson().toJson(new Date(storeTimestamp)));
        long storeHost = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的BornHost:[{}]", storeHost);
        int reconsumeTimes = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的ReconsumeTimes:[{}]", reconsumeTimes);
        long prepareTransactionOffset = mappedByteBuffer.getLong();
        LOGGER.debug("当前消息的PrepareTransactionOffset:[{}]", prepareTransactionOffset);
        int bodyLength = mappedByteBuffer.getInt();
        LOGGER.debug("当前消息的BodyLength:[{}]", bodyLength);
        byte[] body = new byte[bodyLength];
        mappedByteBuffer.get(body);
        LOGGER.debug("当前消息的Body:[{}]", new String(body));
        int topicLentgh = mappedByteBuffer.get();
        LOGGER.debug("当前消息的TopicLentgh:[{}]", topicLentgh);
        byte[] topic = new byte[topicLentgh];
        mappedByteBuffer.get(topic);
        LOGGER.debug("当前消息的Topic:[{}]", new String(topic));
        int propertiesLength = mappedByteBuffer.getShort();
        LOGGER.debug("当前消息的PropertiesLength:[{}]", propertiesLength);
        byte[] properties = new byte[propertiesLength];
        mappedByteBuffer.get(properties);
        LOGGER.debug("当前消息的Properties:[{}]", new String(properties));
    }

    private void init() throws IOException {
        FileChannel channel = new RandomAccessFile(new File(filePath), "rw").getChannel();
        mappedByteBuffer = channel.map(MapMode.READ_ONLY, 0, 1024 * 1024 * 20);
    }

}
