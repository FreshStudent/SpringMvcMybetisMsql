package com.myprojct.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/helloworld")
   //  访问路径   /helloworld/**  都会被拦截
public class HelloWorldController {
	
	//  访问路径  http://localhost:8080/helloworld/index
    @RequestMapping(value="/index", method = {RequestMethod.GET})  //method = {RequestMethod.GET} : 只接受来至 get的请求
    public ModelAndView index(){
        
        ModelAndView modelAndView = new ModelAndView();  
        modelAndView.addObject("message", "Hello World!");  
        modelAndView.setViewName("index.jsp");  
        return modelAndView;
    }
    
    
    /**
     * 获取来自form表单的数据
     * 
     */
    @RequestMapping(value="/getFormInfo")
    public ModelAndView getFormInfo(HttpServletRequest request,HttpServletResponse response){
    	ModelAndView mav = new ModelAndView();
    	String name = request.getParameter("name");
    	String hobby = request.getParameter("hobby");
    	String work = request.getParameter("work");
    
    	/*try {
    		System.out.println(new String(name.getBytes("ISO8859_1"), "GBK"));
        	System.out.println(new String(name.getBytes("ISO8859_1"), "UTF-8"));
        	System.out.println(new String(name.getBytes("ISO8859_1"), "GB2312"));
        	System.out.println(new String(name.getBytes("GBK"), "UTF-8"));
			System.out.println(new String(name.getBytes("GBK"), "GB2312"));
			System.out.println(new String(name.getBytes("GBK"), "ISO8859_1"));
	    	System.out.println(new String(name.getBytes("UTF-8"), "GB2312"));
	    	System.out.println(new String(name.getBytes("UTF-8"), "GBK"));
	    	System.out.println(new String(name.getBytes("UTF-8"), "ISO8859_1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	mav.addObject("name", name);
    	mav.addObject("hobby", hobby);
    	mav.addObject("work", work);
    	//mav.setViewName("showFormInfo.jsp");
    	mav.setViewName("formInfo.jsp");
    	return mav;
    }
    
   /* */
    /**
     * spring 单文件上传
     *//*
    @RequestMapping(value="/fileUpload")
    public ModelAndView fileUpload(HttpServletRequest request,HttpServletResponse response,MultipartFile file){
    	ModelAndView mav = new ModelAndView();
    	//String text = request.getParameter("text");
    	//String fileUpload = request.getParameter("flie");
    	mav.setViewName("fileUpload.jsp");
    	if(!fileUpload.isEmpty()){

    	}
    	return null;
    }*/
    
    
    
}


