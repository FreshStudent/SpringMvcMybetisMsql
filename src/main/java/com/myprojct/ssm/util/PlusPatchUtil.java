package com.myprojct.ssm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlusPatchUtil {

		public static String projectPath = "//Users//liquanliang//git//FZS_FLOW_PLATFORM 3";//项目文件夹路径
	
		public static String patchFile = projectPath+"//change.txt";//补丁文件,由git diff tag1 tag2 --name-only 生成  
     
	    public static String classPath = projectPath+"//target//fzsFlow//WEB-INF//classes";//class项目存放路径  

	    public static String resourcesPath = projectPath+"//target//fzsFlow//resources";//resources存放路径 
	      
	    public static String desPath = projectPath+"//update_pkg";//补丁文件包存放路径  
	      
	    public static String version = "20140711";//补丁版本  
	     
	    
	    //git diff tag1 tag2 --name-only    所输出的log日志，直接copy过来就好
	    public static String changeLogStr = "src/main/java/com/fzs/flow/bean/FlowChannel.java\n" + 
	    		"src/main/java/com/fzs/flow/dao/FlowTransactionDao.java\n" + 
	    		"src/main/java/com/fzs/flow/service/impl/FlowChargeServiceImpl.java\n" + 
	    		"src/main/resources/conf/sql/FlowChannelDao.xml\n" + 
	    		"src/main/resources/conf/sql/FlowTransactionDao.xml\n" + 
	    		"src/main/webapp/WEB-INF/jsp/admin/channel/subChannelEdit.jsp\n" + 
	    		"src/main/webapp/resources/js/admin/channel/subChannelConf.js";
	    
	    /** 
	     * @param args 
	     * @throws Exception  
	     */  
	    public static void main(String[] args) throws Exception {  
//	        copyFiles(getPatchFileList());    //读取Txt的方式
	        copyFiles(getPatchFileList(changeLogStr));   //读取Str的方式
	    } 
	    
	    public static List<String> getPatchFileList(String changeLogStr) throws Exception{  
	        String str[] = changeLogStr.split("\n");
	        return Arrays.asList(str);
	    }  
	    
	    public static List<String> getPatchFileList() throws Exception{  
	        List<String> fileList=new ArrayList<String>();  
	        FileInputStream f = new FileInputStream(patchFile);   
	        BufferedReader dr=new BufferedReader(new InputStreamReader(f,"utf-8"));  
	        String line;  
	        while((line=dr.readLine())!=null){   
	            fileList.add(line);  
	        }   
	        return fileList;  
	    }  
	      
	    public static void copyFiles(List<String> list){  
	          
	        for(String fullFileName:list){  
	            if(fullFileName.indexOf("src/main/java/")!=-1){//对源文件目录下的文件处理  
	                String fileName=fullFileName.replace("src/main/java","");  
	                fullFileName=classPath+fileName;  
	                if(fileName.endsWith(".java")){  
	                    fileName=fileName.replace(".java",".class");  
	                    fullFileName=fullFileName.replace(".java",".class");  
	                }  
	                String tempDesPath=fileName.substring(0,fileName.lastIndexOf("/"));  
	                String desFilePathStr=desPath+"/"+version+"/WEB-INF/classes"+tempDesPath;  
	                String desFileNameStr=desPath+"/"+version+"/WEB-INF/classes"+fileName;  
	                File desFilePath=new File(desFilePathStr);  
	                if(!desFilePath.exists()){  
	                    desFilePath.mkdirs();  
	                }  
	                copyFile(fullFileName, desFileNameStr);  
	                System.out.println(fullFileName+"复制完成");  
	            }else if(fullFileName.indexOf("/resources/")!=-1){//对resource目录进行处理
	            		
	            		String resourceFullPath = null; //将要复制文件全路径
	            		String desFullPath = null;      //复制文件后需要放置路径
	            	
	            		String desFileName=fullFileName.substring(fullFileName.indexOf("/resources/"),fullFileName.length());
	            		resourceFullPath = projectPath+"/"+fullFileName;//将要复制的文件全路径 
	            		desFullPath = desPath+"/"+version+desFileName;  
	            		File desFilePath=new File(desFullPath.substring(0,desFullPath.lastIndexOf("/")));  
 	                if(!desFilePath.exists()){  
 	                    desFilePath.mkdirs();  
 	                }
	            		copyFile(resourceFullPath, desFullPath);  
	            		System.out.println(resourceFullPath+"复制完成"); 
	            		
	            } else if(fullFileName.indexOf("/WEB-INF/")!=-1 && fullFileName.endsWith(".jsp")) { //处理JSP

	            		String resourceFullPath = null; //将要复制文件全路径
	            		String desFullPath = null;      //复制文件后需要放置路径
	            		String desFileName=fullFileName.substring(fullFileName.indexOf("/WEB-INF/"),fullFileName.length());
	            		resourceFullPath = projectPath+"/"+fullFileName;//将要复制的文件全路径 
	            		desFullPath = desPath+"/"+version+desFileName;  
	            		File desFilePath=new File(desFullPath.substring(0,desFullPath.lastIndexOf("/")));  
		                if(!desFilePath.exists()){  
		                    desFilePath.mkdirs();  
		                }
	            		copyFile(resourceFullPath, desFullPath);  
	            		System.out.println(resourceFullPath+"复制完成");
	            	
	            }
	        }  
	          
	    }  
	  
	    private static void copyFile(String sourceFileNameStr, String desFileNameStr) {  
	        File srcFile=new File(sourceFileNameStr);  
	        File desFile=new File(desFileNameStr);  
	        try {  
	            copyFile(srcFile, desFile);  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	      
	  
	      
	      
	    public static void copyFile(File sourceFile, File targetFile) throws IOException {  
	        BufferedInputStream inBuff = null;  
	        BufferedOutputStream outBuff = null;  
	        try {  
	            // 新建文件输入流并对它进行缓冲  
	            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));  
	  
	            // 新建文件输出流并对它进行缓冲  
	            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));  
	  
	            // 缓冲数组  
	            byte[] b = new byte[1024 * 5];  
	            int len;  
	            while ((len = inBuff.read(b)) != -1) {  
	                outBuff.write(b, 0, len);  
	            }  
	            // 刷新此缓冲的输出流  
	            outBuff.flush();  
	        } finally {  
	            // 关闭流  
	            if (inBuff != null)  
	                inBuff.close();  
	            if (outBuff != null)  
	                outBuff.close();  
	        }  
	    }  
	
}
