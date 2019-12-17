package com.activemq.messagequeue.test.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class ActiveMQ {

    private static final String USERNAME = "admin"; // 默认连接
    private static final String PASSWORD = "admin"; // 默认密码
    private static final String URL = "failover://tcp://localhost:61616";

    //编写消息的发送方[生产者]
    @Test
    public void test1() throws JMSException {
        //创建连接工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,URL);
        // 从工厂中获取一个连接对象
        Connection connection = connectionFactory.createConnection();
        //连接MQ服务
        connection.start();
        //获取session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //通过session对象创建Topic
        Topic topic = session.createTopic("MQ-Test");
        //通过session对象创建消息的发送着
        MessageProducer producer = session.createProducer(topic);
        //通过session创建消息对象
        TextMessage message = session.createTextMessage("小名Test");
        //发送消息
        producer.send(message);
        /**
         * 
         * connection.createSession(paramA,paramB);中的paramA为true
         * 1、true：支持事务
         * 为true时：paramB的值忽略， acknowledgment mode被jms服务器设置为SESSION_TRANSACTED 。 　
         * 2、false：不支持事务
         * 为false时：paramB的值可为Session.AUTO_ACKNOWLEDGE、Session.CLIENT_ACKNOWLEDGE、DUPS_OK_ACKNOWLEDGE其中一个。
         */
        //session.commit();

        System.out.println("消息发布者："+message.getText());

        //关闭相关资源
        producer.close();
        session.close();
        connection.close();
    }

    //编写消息的接收方[消费者]
    @Test
    public void test2() throws JMSException {

        //创建连接工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,URL);
        //从工厂中获取一个连接对象
        Connection connection = connectionFactory.createConnection();
        //连接MQ服务
        connection.start();
        //获取session对象

        /*自动应答*/
        //Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        /*手动应答*/
        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);

        //通过session对象创建Topic
        Topic topic = session.createTopic("MQ-Test");
        //通过session对象创建消息的消费者
        MessageConsumer consumer = session.createConsumer(topic);

        //指定消息监听器
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    if(textMessage.getText().toString().equals("小名Test")){
                        System.out.println("消费者接收到了"+textMessage.getText());
                        message.acknowledge();
                    }else{
                        /**
                         * 测试 消息处理失败 需要修改为createTextMessage("小名Test1111");
                         * TextMessage message = session.createTextMessage("小名Test1111");
                         */
                        System.out.println("消息处理失败");
                        //通知MQ进行消息重复，最多可以重复6次
                        session.recover();
                        //模拟消息失败
                        int i= 1/0;
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //保证消费端一直在线
        while(true){}
    }

}
