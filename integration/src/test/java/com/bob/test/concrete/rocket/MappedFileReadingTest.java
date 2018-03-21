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
 * MappedFile信息读取,包括CommitLog,ConsumeQueue,IndexFile
 *
 * @author wb-jjb318191
 * @create 2018-03-16 14:20
 */
public class MappedFileReadingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MappedFileReadingTest.class);

    private String filePath;
    private MappedByteBuffer mappedByteBuffer;

    @Test
    public void testReadConsumeQueue() throws IOException {
        filePath = "C:\\Users\\Administrator\\store\\consumequeue\\test-topic\\1\\00000000000000000000";
        init(0, 1000 * 1000 * 6);
        do {
            long offset = mappedByteBuffer.getLong();
            if (offset == 0) {
                break;
            }
            int size = mappedByteBuffer.getInt();
            long code = mappedByteBuffer.getLong();
            LOGGER.debug("CommitLog Offset:[{}],TotalSize:[{}],TagsCode:[{}]", offset, size, code);
        } while (true);
    }

    @Test
    public void testReadIndexFile() throws IOException {
        filePath = "C:\\Users\\Administrator\\store\\index\\20180317152149902";
        init(0, 40 + 5000000 * 4 + 5000000 * 4 * 20);
        readIndexFileHeader(mappedByteBuffer);
        mappedByteBuffer.position(40 + 5000000 * 4);
        for (int i = 0; i < 100; i++) {
            LOGGER.debug("keyHash:[{}],phyOffset:[{}],timeDiff:[{}],slotValue:[{}]", mappedByteBuffer.getInt(), mappedByteBuffer.getInt(),
                mappedByteBuffer.getLong(), mappedByteBuffer.getInt());
        }
    }

    @Test
    public void testReadCommitLog() throws IOException {
        filePath = "C:\\Users\\Administrator\\store\\commitlog\\00000000000000000000";
        init(6078, 1024 * 1024 * 1024);
        readCommitLog();
    }

    private void init(int position, long size) throws IOException {
        FileChannel channel = new RandomAccessFile(new File(filePath), "r").getChannel();
        mappedByteBuffer = channel.map(MapMode.READ_ONLY, 0, size);
        mappedByteBuffer.position(position);
    }

    private void readIndexFileHeader(MappedByteBuffer mappedByteBuffer) {
        long beginTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("IndexFile Header BeginTimestamp:[{}]", new Gson().toJson(new Date(beginTimestamp)));
        long endTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("IndexFile Header EndTimestamp:[{}]", new Gson().toJson(new Date(endTimestamp)));
        long beginPhyOffset = mappedByteBuffer.getLong();
        LOGGER.debug("IndexFile Header BeginPhyOffset:[{}]", beginPhyOffset);
        long endPhyOffset = mappedByteBuffer.getLong();
        LOGGER.debug("IndexFile Header EndPhyOffset:[{}]", endPhyOffset);
        int hashSlotCount = mappedByteBuffer.getInt();
        LOGGER.debug("IndexFile Header HashSlotCount:[{}]", hashSlotCount);
    }

    private void readCommitLog() {
        int size = mappedByteBuffer.getInt();
        LOGGER.debug("Message TotalSize:[{}]", size);
        int magicCode = mappedByteBuffer.getInt();
        LOGGER.debug("Message MagicCode:[{}]", magicCode);
        int bodyRPC = mappedByteBuffer.getInt();
        LOGGER.debug("Message BodyRPC:[{}]", bodyRPC);
        int queueId = mappedByteBuffer.getInt();
        LOGGER.debug("Message QueueId:[{}]", queueId);
        int flag = mappedByteBuffer.getInt();
        LOGGER.debug("Message Flag:[{}]", flag);
        long queueOffset = mappedByteBuffer.getLong();
        LOGGER.debug("Message ConsumeQueue Offset:[{}]", queueOffset);
        long physicalOffset = mappedByteBuffer.getLong();
        LOGGER.debug("Message CommitLog Offset:[{}]", physicalOffset);
        int sysFlag = mappedByteBuffer.getInt();
        LOGGER.debug("Message SysFlag:[{}]", sysFlag);
        long bornTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("Message BornTimestamp:[{}]", new Gson().toJson(new Date(bornTimestamp)));
        long bornHost = mappedByteBuffer.getLong();
        LOGGER.debug("Message BornHost:[{}]", bornHost);
        long storeTimestamp = mappedByteBuffer.getLong();
        LOGGER.debug("Message BornTimestamp:[{}]", new Gson().toJson(new Date(storeTimestamp)));
        long storeHost = mappedByteBuffer.getLong();
        LOGGER.debug("Message BornHost:[{}]", storeHost);
        int reconsumeTimes = mappedByteBuffer.getInt();
        LOGGER.debug("Message ReconsumeTimes:[{}]", reconsumeTimes);
        long prepareTransactionOffset = mappedByteBuffer.getLong();
        LOGGER.debug("Message PrepareTransactionOffset:[{}]", prepareTransactionOffset);
        int bodyLength = mappedByteBuffer.getInt();
        LOGGER.debug("Message BodyLength:[{}]", bodyLength);
        byte[] body = new byte[bodyLength];
        mappedByteBuffer.get(body);
        LOGGER.debug("Message Body:[{}]", new String(body));
        int topicLentgh = mappedByteBuffer.get();
        LOGGER.debug("Message TopicLentgh:[{}]", topicLentgh);
        byte[] topic = new byte[topicLentgh];
        mappedByteBuffer.get(topic);
        LOGGER.debug("Message Topic:[{}]", new String(topic));
        int propertiesLength = mappedByteBuffer.getShort();
        LOGGER.debug("Message PropertiesLength:[{}]", propertiesLength);
        byte[] properties = new byte[propertiesLength];
        mappedByteBuffer.get(properties);
        LOGGER.debug("Message Properties:[{}]", new String(properties));
    }

}
