package com.myprojct.ssm.rabbitmqSpringDemo;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

	private static Logger logger = Logger.getLogger(MessageConsumer.class);

	@Resource
	private AmqpTemplate amqpTemplate;

	public String receiveMessage(String queueKey) {
		try {
			String message = (String) amqpTemplate.receiveAndConvert(queueKey);
			if (!StringUtils.isEmpty(message)) {
				logger.info("from " + queueKey + " receive message:" + message);
			}
			return message;
		} catch (Exception ex) {
			logger.error("from " + queueKey + " receive message, happens error", ex);
		}
		return null;
	}
}
