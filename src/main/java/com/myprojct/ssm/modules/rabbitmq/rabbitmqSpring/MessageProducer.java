package com.myprojct.ssm.modules.rabbitmq.rabbitmqSpring;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

	private static Logger logger = Logger.getLogger(MessageProducer.class);

	@Resource
	private AmqpTemplate amqpTemplate;

	public void sendMessage(String queueKey, String message) {
		logger.info("to " + queueKey + " send message:" + message);
		try {
			amqpTemplate.convertAndSend(queueKey, message);
		} catch (Exception ex) {
			logger.error("to " + queueKey + " send message:" + message + " ,happens error", ex);
			throw new RuntimeException("MessageProducer error:" + queueKey);
		}
	}

	public void sendDelayMessage(String queueKey, String message, final int delayMilliSecond) {
		logger.info("to " + queueKey + " send message:" + message);
		try {
			amqpTemplate.convertAndSend(queueKey, message);
			/*
			 * amqpTemplate.convertAndSend(queueKey, (Object)message, new
			 * MessagePostProcessor() {
			 * 
			 * @Override public Message postProcessMessage(Message message) throws
			 * AmqpException { //设置延迟时间
			 * //message.getMessageProperties().setDelay(delayMilliSecond);
			 * //message.getMessageProperties().setExpiration(delayMilliSecond); return
			 * message; } });
			 */

		} catch (Exception ex) {
			logger.error("to " + queueKey + " send message:" + message + " ,happens error", ex);
			throw new RuntimeException("MessageProducer error:" + queueKey);
		}
	}

}
