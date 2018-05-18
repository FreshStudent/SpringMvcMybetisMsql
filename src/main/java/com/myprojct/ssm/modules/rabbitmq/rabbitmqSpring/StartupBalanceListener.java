package com.myprojct.ssm.modules.rabbitmq.rabbitmqSpring;
//package com.fzs.flow.modules.recharge.mq.consumer;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * 主要为了启动spring容器后，执行设定好的方法。
// * 
// */
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Service;
//
//import com.fzs.flow.bean.ResultBean;
//import com.fzs.flow.modules.recharge.constants.RechargeConstants;
//import com.fzs.flow.modules.recharge.services.ChargeBalanceService;
//import com.fzs.flow.service.action.FlowChargeActionResult;
//
///**
// * 主要为了启动spring容器后，执行余额变动队列监听方法。
// * @author lql
// * @date：2017-10-16
// *
// */
//
//@Service
//public class StartupBalanceListener implements ApplicationListener<ContextRefreshedEvent> {
//
//	private static final Logger logger = Logger.getLogger(StartupBalanceListener.class);
//
//	@Autowired
//	private MessageConsumer messageConsumer;
//
//	@Autowired
//	private ChargeBalanceService chargeBalanceService;
//	
//	private boolean running = true;
//
//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent evt) {
//		if (running&&evt.getApplicationContext().getParent() == null) { // root application context 没有parent
//			//final MessageConsumer messageConsumer = (MessageConsumer) evt.getApplicationContext().getBean("messageConsumer");
//			System.out.println("Spring已经启动了！！");
//			
////			HandleBalance handleBalance = new HandleBalance();
////			handleBalance.start();
//				
//		}
//	}
//
//	class HandleBalance extends Thread {
//	    /*
//	     * 覆写run()方法，定义该线程需要执行的代码
//	     */
//	    @Override
//	    public void run() {
//	    	while (running) {	
//				String jsonStr = messageConsumer.receiveMessage(RechargeConstants.QUEUE_FLOW_CHARGE_BALANCE);
//				if(!StringUtils.isEmpty(jsonStr)) {
//					//开始解析字符串
//					System.out.println("message："+jsonStr);
//					
//					try {
//						
//						Thread.sleep(500);
//						FlowChargeActionResult<ResultBean> result = chargeBalanceService.hanlderBalanceQueen(jsonStr);
//						
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//			}
//	    }
//	}
//	
//	
//	
//	
//	public boolean isRunning() {
//		return running;
//	}
//
//	public void setRunning(boolean running) {
//		this.running = running;
//	}
//
//}
//;