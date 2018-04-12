package com.myprojct.ssm.rabbitmqSpringDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * 主要为了启动spring容器后，执行设定好的方法。
 * 通过线程来获取Rabbit中的信息
 * 
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(StartupListener.class);

	private ExecutorService executor = Executors.newFixedThreadPool(4);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {
		if (evt.getApplicationContext().getParent() == null) { // root application context 没有parent

			
			//debug日志太多 先屏蔽 2018-03-02  begin
//			final MessageConsumer messageConsumer = (MessageConsumer) evt.getApplicationContext().getBean("messageConsumer");
//
//			try {
//				executor.execute(new Runnable() {
//					public void run() {
//						while (true) {
//							messageConsumer.receiveMessage("flowChangeOrder");
//						}
//					}
//				});
//			} catch (Exception e) {
//				logger.error("ChannelStausFlowSyncCus" + "channelName" + " ,happens error", e);
//			}
			//debug日志太多 先屏蔽 2018-03-02  end

		}
	}

}
