package com.activemq.messagequeue.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 消息持久化订阅测试
 *
 * @Author XiaoMing
 */
public class TopicPersistentTest {

    private static final String PASSWORD = "admin"; // 默认密码
    private static final String URL = "failover://tcp://localhost:61616";
    private static final String USERNAME = "admin"; // 默认连接

    //编写消息的发送方[生产者]
    @Test
    public void test1() throws JMSException {
        //创建连接工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,URL);
        //从工厂中获取一个连接对象
        Connection connection = connectionFactory.createConnection();
        //连接MQ服务
        connection.start();
        //获取session对象
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //通过session对象创建Topic
        Topic topic = session.createTopic("Topic-Test");
        //通过session对象创建消息的发送者
        MessageProducer producer = session.createProducer(topic);
        //通过session创建消息对象
        TextMessage message = session.createTextMessage("Topic测试");
        //发送消息 DeliveryMode.PERSISTENT使用持久化;优先级
        producer.send(message,DeliveryMode.PERSISTENT,1,1000*60*60*24);
        //关闭相关资源
        producer.close();
        session.close();
        connection.close();
    }

    //编写消息的接收方[消费者]
    @Test
    public void test2() throws JMSException {
        //创建连接工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, URL);
        //从工厂中获取一个连接对象
        Connection connection = connectionFactory.createConnection();
        //设置客户端id
        connection.setClientID("client-1");
        //连接MQ服务
        connection.start();
        //获取session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //通过session对象创建Topic
        Topic topic = session.createTopic("Topic-Test");
        //客户端持久化订阅
        TopicSubscriber consumer = session.createDurableSubscriber(topic, "client1-sub");
        //通过session对象创建消息的消费者
        //MessageConsumer consumer = session.createConsumer(topic);
        //指定消息监听器
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
               TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("消费者接收到消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //保证消费端一直在线
        while(true){}
    }


}
