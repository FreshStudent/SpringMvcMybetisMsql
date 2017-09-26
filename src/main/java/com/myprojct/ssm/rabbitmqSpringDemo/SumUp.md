
总结：
原来用RabbitMq的原生方式实现了mq的发送和接受，后来由于技术要求，需要使用Spring来整合mq，因此改变了策略。


#### 一、原生官网RabbitMq的关键步骤：
1.利用brew来安装RabbitMq，然后启动Mq服务器<br>
2.根据RabbitMq官网的Java例子来模拟mq的消息生产者(Send)、消息消费者(Producer)<br>
3.根据本地启动的RabbitMq的服务器，可以查看已经发送的mq队列消息


#### 二、将RabbitMq整合到Spring中，步骤：<br>
1.在spring的配置文件中引入RabbitMq的配置文件（import）<br>
2.RabbitMq的配置文件中，需要配置mq的Connection的相关信息,涉及服务器路径、用户名、用户密码、 
  端口（注意这里的端口不是后台的15672，是5672，需要和后台访问路径中的端口号区分开来）。<br>
3.因为需要验证配置文件是否正确，所以可以利用Main函数来加载配置文件，相关的代码如下：


``` java

ApplicationContext ct = new ClassPathXmlApplicationContext(new String[] {"SpringConf.xml"});
/** 
 * 打印mq的连接信息，用于验证XML是否正确读取property文件
 */
ConnectionFactory factory = (ConnectionFactory)ct.getBean("rabbitConnectionFactory");
System.out.println("host:" + factory.getHost());
System.out.println("port:" + factory.getPort());
System.out.println("username:" + factory.getUsername());
System.out.println("password:" + factory.getPassword());	
/**
 * 运用配置好的Spring整合RabbitMq来发送信息
 */
MessageProducer messageProducer = (MessageProducer)ct.getBean("messageProducer");
messageProducer.sendMessage("flowChangeOrder", "HelloTest");
messageProducer.sendMessage("flowChangeOrder", "HelloTest1");
messageProducer.sendMessage("flowChangeOrder", "HelloTest2");
messageProducer.sendMessage("flowChangeOrder", "HelloTest3");
messageProducer.sendMessage("flowChangeOrder", "HelloTest4");
messageProducer.sendMessage("flowChangeOrder", "HelloTest5");
```

