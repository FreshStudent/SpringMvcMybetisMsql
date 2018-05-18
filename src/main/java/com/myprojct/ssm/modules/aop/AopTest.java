package com.myprojct.ssm.modules.aop;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/aopTest")
   //  访问路径   /helloworld/**  都会被拦截
public class AopTest {
	
	private static final Logger logger = Logger.getLogger(AopTest.class);
	
	//  访问路径 http://localhost:8180//SpringMvcMybetisMsql/aopTest/index.spring?email=%22ads%22
    @RequestMapping(value="/index", method = {RequestMethod.GET})  
    public ModelAndView aopTest(String email){
		logger.info("sayHello to >>>>{}"+email);
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.addObject("message", "AopTest！！！！");  
		modelAndView.setViewName("aopTest.jsp");  
		return modelAndView;
    }
    
    //  访问路径 http://localhost:8180//SpringMvcMybetisMsql/aopTest/index1.spring?email=%22ads%22
    @RequestMapping(value="/index1", method = {RequestMethod.GET})  
    public ModelAndView aopTest1(String email){
		logger.info("sayHello to >>>>{}"+email);
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.addObject("message", "AopTest1！！！！");  
		modelAndView.setViewName("aopTest.jsp");  
		return modelAndView;
    }
    
}