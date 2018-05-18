
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

三、RabbitMQ的结构图
![RabbitMQ的结构图](https://github.com/FreshStudent/SpringMvcMybetisMsql/blob/master/src/main/java/com/myprojct/ssm/rabbitmqSpringDemo/RabbitMqSumPic.png)

四、几个概念说明：

Broker：简单来说就是消息队列服务器实体。
Exchange：消息交换机，它指定消息按什么规则，路由到哪个队列。
Queue：消息队列载体，每个消息都会被投入到一个或多个队列。
Binding：绑定，它的作用就是把exchange和queue按照路由规则绑定起来。
Routing Key：路由关键字，exchange根据这个关键字进行消息投递。
vhost：虚拟主机，一个broker里可以开设多个vhost，用作不同用户的权限分离。
Producer：消息生产者，就是投递消息的程序。
Consumer：消息消费者，就是接受消息的程序。
Channel：消息通道，在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务。

五、Rabbitmq的工作原理：
1、客户端连接到消息队列服务器，打开channel。
2、客户端声明一个exchange，并设置相关属性。
3、客户端声明一个queue，并设置相关属性。
4、客户端使用routing key，在exchange和queue之间建立好绑定关系，进行消息路由，将消息投递到一个或者多个队列里。
5、客户端投递消息到exchange。
6、exchange接收到消息后，就根据消息的key和已经设置的binging，进行消息路由，将消息投递到一个或者多个队列里。
