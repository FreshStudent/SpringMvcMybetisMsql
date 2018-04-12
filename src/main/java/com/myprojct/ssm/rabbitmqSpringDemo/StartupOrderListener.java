package com.myprojct.ssm.rabbitmqSpringDemo;
//package com.fzs.flow.modules.recharge.mq.consumer;
//
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
///**
// * 主要为了启动spring容器后，执行监听订单变动队列方法。
// * @author lql
// * @date：2017-10-16
// * 
// */
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Service;
//import org.springframework.amqp.core.Queue;
//
//import com.fzs.flow.bean.ResultBean;
//import com.fzs.flow.modules.recharge.constants.RechargeConstants;
//import com.fzs.flow.modules.recharge.entities.OrderParamsVo;
//import com.fzs.flow.modules.recharge.services.ChargeOrderService;
//import com.fzs.flow.modules.recharge.services.ChargeService;
//import com.fzs.flow.service.action.FlowChargeActionResult;
//import com.fzs.flow.util.Jacksons;
//import com.rabbitmq.client.Channel;
//
//@Service
//public class StartupOrderListener implements ApplicationListener<ContextRefreshedEvent> {
//
//	private static final Logger logger = Logger.getLogger(StartupOrderListener.class);
//
//	@Autowired
//	private MessageConsumer messageConsumer;
//
//	@Autowired
//	private ChargeService chargeService;
//	@Autowired
//	private ChargeOrderService chargeOrderService;
//	
//	@Autowired
//	@Qualifier("flowChargeOrder")
//	private Queue orderCharge;
//	
//	
//	private boolean running = true;
//
//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent evt) {
//		if (running&&evt.getApplicationContext().getParent() == null) { // root application context 没有parent
//			//final MessageConsumer messageConsumer = (MessageConsumer) evt.getApplicationContext().getBean("messageConsumer");
//			System.out.println("Spring已经启动了！！");
//			
////			HandleOrder handleOrder = new HandleOrder();
////			handleOrder.start();
//				
//		}
//	}
//
//	class HandleOrder extends Thread {
//	    /*
//	     * 覆写run()方法，定义该线程需要执行的代码
//	     */
//	    @Override
//	    public void run() {
//	    	while (running) {	
//				String orderParamsJsonStr = messageConsumer.receiveMessage(RechargeConstants.QUEUE_FLOW_CHARGE_ORDER);
//				
//				if(!StringUtils.isEmpty(orderParamsJsonStr)) {
//					//开始解析字符串
//					logger.info("订单队里中取出的数据jsonStr：" + orderParamsJsonStr);
//					
//					OrderParamsVo orderParamsVo = Jacksons.parse(orderParamsJsonStr, OrderParamsVo.class);
//					try {
//						Thread.sleep(500);
//						FlowChargeActionResult<ResultBean> result = chargeOrderService.hanlderOrderQueen(orderParamsVo);
//						ResultBean rb = result.getResultEntity();
//						
//						if(!rb.getSuccess()) { //失败，根据回调地址返回错误信息
//							chargeService.validReturnXml(orderParamsVo, rb);
//						}
//					
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//	    }
//	}
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