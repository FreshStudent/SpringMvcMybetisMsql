package com.myprojct.ssm.modules.rabbitmq.rabbitmqSpring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqProducer {
	
	
	
	public static void main(String args[]) {
		
		try {
		
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
		 * 利用原生的RabbitMq，检验是否能正确发送RabbitMq
		 */
		// 创建一个连接
//		Connection connection;
//		
//		connection = factory.newConnection();
//
//		// 创建一个频道
//		Channel channel = connection.createChannel();
//		// 指定一个队列
//		channel.queueDeclare("flowChangeOrder", false, false, false, null);
//		
//		// 发送的消息
//		String message = "hello world you are doubihahah!";
//		
//		// 往队列中发出一条消息
//		
//			channel.basicPublish("", "flowChangeOrder", null, message.getBytes());
//		
//		System.out.println(" [x] Sent '" + message + "'");
//		// 关闭频道和连接
//		channel.close();
//		connection.close();
		
		
		
		/**
		 * 运用配置好的Spring整合RabbitMq来发送信息
		 */
		MessageProducer messageProducer = (MessageProducer)ct.getBean("messageProducer");
		for(int i=0;i<10000;i++) {
			System.out.println("sending message :"+i);
			messageProducer.sendMessage("flowChangeOrder", "YYYYY");
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
