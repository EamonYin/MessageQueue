# MessageQueue
基于ActiveMQ的消息队列Demo  
1. 下载ActiveMQ:http://mirror.bit.edu.cn/apache//activemq/5.15.9/apache-activemq-5.15.9-bin.zip  
2. 解压 -> 运行 apache-activemq-5.15.9\bin\win64\activemq.bat  
3. 浏览器 localhost:8161 -> 用户名：admin 密码：admin ->Topics  
4. 先运行 “test2()”-消费者端 ； 再运行“test1()”-发布者端  
编写消息的发送方[生产者]  
# 创建连接工厂对象  
从工厂中获取一个连接对象  
连接MQ服务  
获取session对象  
通过session对象创建Topic  
通过session对象创建消息的发送者  
通过session创建消息对象  
发送消息  
关闭相关资源  

# 编写消息的接收方[消费者]  
创建连接工厂对象  
从工厂中获取一个连接对象  
连接MQ服务  
获取session对象  
通过session对象创建Topic  
通过session对象创建消息的消费者  
指定消息监听器  
保证消费端一直在线  
---------------------------------------------------
通过替换
GitHub\messagequeue\apache-activemq-5.15.9\conf
中的 activemq.xml 文件
可以分别测试
本地持久化（ActiveMQ类） 和 mysql持久化（TopicPersistentTest类）
