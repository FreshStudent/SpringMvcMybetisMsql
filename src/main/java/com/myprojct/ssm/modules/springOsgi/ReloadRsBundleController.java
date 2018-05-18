package com.myprojct.ssm.modules.springOsgi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/reloadRsBundle")
   //  访问路径   /helloworld/**  都会被拦截
public class ReloadRsBundleController {
	
	
	@Autowired
	private ApplicationContext applicationContext;
	
	
	//  访问路径  http://localhost:8180/SpringMvcMybetisMsql/reloadRsBundle/index.spring
    @RequestMapping(value="/index", method = {RequestMethod.GET})  //method = {RequestMethod.GET} : 只接受来至 get的请求
    public ModelAndView index(){
        
        ModelAndView modelAndView = new ModelAndView();  
        String reloadMessag = applicationContext.getMessage("reload.message", null, null);
        modelAndView.addObject("message", reloadMessag);  
        modelAndView.setViewName("index.jsp");  
        return modelAndView;
    }
    
    
    
    
    
}


