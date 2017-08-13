package com.myprojct.ssm.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定式任务：1、基于注解方式配置定式任务（此Demo主要是使用这种），还有另外一种是使用xml来配置定式任务 
 * 好处：使用注解方式不需要再每写一个任务类还要在xml文件中配置下，方便了很多。使用Spring的@Scheduled
 * @author liquanliang
 * 参考链接：http://blog.csdn.net/u010648555/article/details/52162840
 *
 */

@Component
public class DemoTask {
	
	 // 每五秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void TaskJob() {
        System.out.println("DemoTask ----test second annotation style ...");
    }
}
