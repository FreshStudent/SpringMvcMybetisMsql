package com.myprojct.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
   //  访问路径   /user/**  都会被拦截
public class UserController {  
    
	
	@RequestMapping(value="/findUser",method = RequestMethod.POST)  
    public ModelAndView getUser(){     
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("findUser.jsp");
		
        return mv;  
    }  
    
    @RequestMapping(value="/index")  
    public ModelAndView getIndex(){ 
    		ModelAndView mv = new ModelAndView();
        mv.setViewName("user.jsp");
    		return mv;  
    }  
  
} 