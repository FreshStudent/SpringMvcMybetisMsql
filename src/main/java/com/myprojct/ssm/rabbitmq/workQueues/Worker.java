package com.myprojct.ssm.rabbitmq.workQueues;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker {
	private static final String TASK_QUEUE_NAME = "task_queue_NewTask22";

	public static void main(String[] argv) throws Exception {
		
		// 打开连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");  
		
		// 创建频道
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();
		
		// 声明队列
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		channel.basicQos(1);

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				System.out.println(" [x] Received '" + message + "'");
				try {
					//doWork(message);
				} finally {
					System.out.println(" [x] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		
		boolean autoAck = false;
		channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
	}

	private static void doWork(String task) {
		for (char ch : task.toCharArray()) {
			if (ch == '-') {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException _ignored) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
