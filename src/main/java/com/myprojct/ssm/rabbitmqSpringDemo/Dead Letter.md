##### Dead Letter  
官方介绍 http://www.rabbitmq.com/dlx.html
通过官网介绍,可以发现Dead Letter 本来不是用于延时发送的, 而是处理 消息过期,消息被拒绝,队列超出限制大小 等情况.  
  
##### DLX, Dead-Letter-Exchange。  
利用DLX, 当消息在一个队列中变成死信（dead message）之后，它能被重新publish到另一个Exchange，这个Exchange就是DLX。消息变成死信一向有一下几种情况：

1. 消息被拒绝（basic.reject/ basic.nack）并且requeue=false
2. 消息TTL过期（参考：RabbitMQ之TTL（Time-To-Live 过期时间））
3. 队列达到最大长度

  
##### 下面是spring对于dead letter的配置
``` xml
<!--需要配置死信队列的queue：dead-letter-queue-->
<rabbit:queue name="dead-letter-queue">  
    <rabbit:queue-arguments>  
        <entry key="x-message-ttl" value="5000" value-type="java.lang.Long" />  
        <entry key="x-dead-letter-exchange" value="common-exchange" />  
        <entry key="x-dead-letter-routing-key" value="common-queue" />  
    </rabbit:queue-arguments>  
</rabbit:queue>  

<!--存放死信的队列：common-queue-->  
<rabbit:queue name="common-queue" />  

<!--死信交换机：common-exchange，利用交换机把信息转发到common-queue中-->
<rabbit:direct-exchange name="common-exchange"  
    durable="false" auto-delete="false" id="common-exchange">  
    <rabbit:bindings>  
        <rabbit:binding queue="common-queue" />  
    </rabbit:bindings>  
</rabbit:direct-exchange>  

```

这是目前项目使用的配置文件：  
```xml

```