package com.myprojct.ssm.modules.rabbitmq.rabbitmqNative.workQueues;

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

	private static final String TASK_QUEUE_NAME = "task_queue_NewTask22";

	public static void main(String[] argv) throws java.io.IOException {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

			String message = getMessage(argv);
            
			//发1000条信息，然后利用worker进行获取
			for(int i=0;i<=1000;i++) {
				String messageTemp = message+"-"+(i);
				channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, messageTemp.getBytes());
				System.out.println(" [x] Sent '" + messageTemp+"'");

			}
			
			channel.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}