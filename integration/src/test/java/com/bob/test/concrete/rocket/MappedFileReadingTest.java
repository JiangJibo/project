package com.bob.test.concrete.rocket;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.sysflag.MessageSysFlag;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.rocketmq.common.message.MessageDecoder.MSG_ID_LENGTH;
import static org.apache.rocketmq.common.message.MessageDecoder.createMessageId;
import static org.apache.rocketmq.common.message.MessageDecoder.string2messageProperties;

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

    public void readCommitLog0() throws UnknownHostException, UnsupportedEncodingException {
        MessageExt msgExt = new MessageExt();
        // 1 TOTALSIZE
        int storeSize = mappedByteBuffer.getInt();
        msgExt.setStoreSize(storeSize);

        // 2 MAGICCODE
        mappedByteBuffer.getInt();

        // 3 BODYCRC
        int bodyCRC = mappedByteBuffer.getInt();
        msgExt.setBodyCRC(bodyCRC);

        // 4 QUEUEID
        int queueId = mappedByteBuffer.getInt();
        msgExt.setQueueId(queueId);

        // 5 FLAG
        int flag = mappedByteBuffer.getInt();
        msgExt.setFlag(flag);

        // 6 QUEUEOFFSET
        long queueOffset = mappedByteBuffer.getLong();
        msgExt.setQueueOffset(queueOffset);

        // 7 PHYSICALOFFSET
        long physicOffset = mappedByteBuffer.getLong();
        msgExt.setCommitLogOffset(physicOffset);

        // 8 SYSFLAG
        int sysFlag = mappedByteBuffer.getInt();
        msgExt.setSysFlag(sysFlag);

        // 9 BORNTIMESTAMP
        long bornTimeStamp = mappedByteBuffer.getLong();
        msgExt.setBornTimestamp(bornTimeStamp);

        // 10 BORNHOST
        byte[] bornHost = new byte[4];
        mappedByteBuffer.get(bornHost, 0, 4);
        int port = mappedByteBuffer.getInt();
        msgExt.setBornHost(new InetSocketAddress(InetAddress.getByAddress(bornHost), port));

        // 11 STORETIMESTAMP
        long storeTimestamp = mappedByteBuffer.getLong();
        msgExt.setStoreTimestamp(storeTimestamp);

        // 12 STOREHOST
        byte[] storeHost = new byte[4];
        mappedByteBuffer.get(storeHost, 0, 4);
        port = mappedByteBuffer.getInt();
        msgExt.setStoreHost(new InetSocketAddress(InetAddress.getByAddress(storeHost), port));

        // 13 RECONSUMETIMES
        int reconsumeTimes = mappedByteBuffer.getInt();
        msgExt.setReconsumeTimes(reconsumeTimes);

        // 14 Prepared Transaction Offset
        long preparedTransactionOffset = mappedByteBuffer.getLong();
        msgExt.setPreparedTransactionOffset(preparedTransactionOffset);

        // 15 BODY
        int bodyLen = mappedByteBuffer.getInt();
        if (bodyLen > 0) {
            byte[] body = new byte[bodyLen];
            mappedByteBuffer.get(body);
            msgExt.setBody(body);
        }

        // 16 TOPIC
        byte topicLen = mappedByteBuffer.get();
        byte[] topic = new byte[(int)topicLen];
        mappedByteBuffer.get(topic);
        msgExt.setTopic(new String(topic, "UTF-8"));

        // 17 properties
        short propertiesLength = mappedByteBuffer.getShort();
        if (propertiesLength > 0) {
            byte[] properties = new byte[propertiesLength];
            mappedByteBuffer.get(properties);
            String propertiesString = new String(properties, "UTF-8");
            Map<String, String> map = string2messageProperties(propertiesString);
            System.out.println(map);
            //msgExt.setProperties(map);
        }

        ByteBuffer byteBufferMsgId = ByteBuffer.allocate(MSG_ID_LENGTH);
        String msgId = createMessageId(byteBufferMsgId, msgExt.getStoreHostBytes(), msgExt.getCommitLogOffset());
        msgExt.setMsgId(msgId);

    }

}
