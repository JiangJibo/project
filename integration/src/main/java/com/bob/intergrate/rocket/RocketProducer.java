package com.bob.intergrate.rocket;

import java.util.Date;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Component;

/**
 * Rocket消息发送者
 *
 * @author wb-jjb318191
 * @create 2018-02-08 17:17
 */
public class RocketProducer {

    public DefaultMQProducer createProducer() {
        DefaultMQProducer producer = new DefaultMQProducer("rmq-group");
        //producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setInstanceName("rmq-instance");
        producer.setVipChannelEnabled(false); // 必须设为false否则连接broker10909端口
        return producer;
    }

    public void produce1() throws MQClientException {
        DefaultMQProducer producer = createProducer();
        //producer.setRetryAnotherBrokerWhenNotStoreOK(true);  //当一个Broker出现存储消息异常时，尝试发送其他的Broker
        producer.start();
        System.out.println("开始发送数据");
        try {
            for (int i = 0; i < 3; i++) {
                Message msg = new Message("test1",// topic
                    "TagA",// tag
                    (new Date() + "这里是一条消息" + i).getBytes()// body
                );
                SendResult sendResult = producer.send(msg);
                System.out.println("发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }

    public static void produce0() throws MQClientException, InterruptedException {
        //声明并初始化一个producer
        //需要一个producer group名字作为构造方法的参数，这里为producer1
        DefaultMQProducer producer = new DefaultMQProducer("producer1");

        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr("10.1.54.121:9876;10.1.54.122:9876");

        //调用start()方法启动一个producer实例
        producer.start();

        //发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message("TopicTest",// topic
                    "TagA",// tag
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)// body
                );

                //调用producer的send()方法发送消息
                //这里调用的是同步的方式，所以会有返回结果
                SendResult sendResult = producer.send(msg);

                //打印返回结果，可以看到消息发送的状态以及一些相关信息
                System.out.println(sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        //发送完消息之后，调用shutdown()方法关闭producer
        producer.shutdown();
    }

}
