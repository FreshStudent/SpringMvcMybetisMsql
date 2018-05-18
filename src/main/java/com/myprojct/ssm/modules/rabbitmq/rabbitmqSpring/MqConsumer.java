package com.myprojct.ssm.modules.rabbitmq.rabbitmqSpring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.QueueingConsumer;

@Component
public class MqConsumer {
	@Autowired
	private MessageConsumer messageConsumer;
	
	String queue = "flowChangeOrder";
	
	private String serviceName;
	private String timeInterval;
	
	public static void main(String args[]) {
		ApplicationContext ct = new ClassPathXmlApplicationContext(new String[] {"SpringConf.xml"});
		MessageConsumer messageConsumer = (MessageConsumer)ct.getBean("messageConsumer");
		
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		messageConsumer.receiveMessage("flowChangeOrder");
		
	}
	
	
	
	
	
	
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public String getInfo(){
		 String message = messageConsumer.receiveMessage(queue);
		 return message;
	}
	
	
	
	
}
