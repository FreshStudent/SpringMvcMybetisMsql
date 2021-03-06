package com.myprojct.ssm.util;


/**
 * 2018-05-08
 * 主要用于增量发布
 * 在项目根目录中，使用命令：git diff tag1 tag2 --name-only 
 * 命令执行后，会产生一个log，复制相关改动文件路径进来即可
 * 
 * 
 * 
 * 使用步骤：
 * 1、在项目根目录中执行 git dff tag1 tag2 --name-only          获取改变文件的log日志
 * 2、将上一步获得的log日志复制到下面的changeLogStr中
 * 3、修改以下的参数
 * 4、运行
 * 5、检查生成的文件数量是否对应，需要注意是否有内部类！！！！！！！！
 * 
 * 
 * 2018-05-09 
 * add：已修改兼容内部类
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PlusPatchUtil {
	
	
		public static String projectPath = "/Users/liquanliang/git/FZS_FLOW_PLATFORM";//本地项目文件夹路径
	
		public static String patchFile = projectPath+"//change.txt";//补丁文件,由git diff tag1 tag2 --name-only 生成  
     
	    public static String classPath = projectPath+"/target/fzsFlow/WEB-INF/classes";//class文件存放路径  

	    public static String resourcesPath = projectPath+"/target/fzsFlow/resources";//resources存放路径 
	      
	    public static String desPath = projectPath+"/update_pkg";//补丁文件包存放路径  文件夹名称：update_pkg
	      
	    public static String version = "20180508";//补丁版本  
	     
	    public static int filesNums = 0;    //已经处理文件总数
	    
	    public static int preFileCount = 0;  //待处理文件总数
	    
	    public static int foundInnerClassFileCount = 0;  //发现内部类次数

	    public static int innerClassFileCount = 0;  //内部类总数，包含源文件

	    public static List<String> innerClassFileUrl = new ArrayList<String>();  //内部类文件列表

	    public static List<String> copyFilesFullUrlLog = new ArrayList<String>();  //记录复制文件的全路径 
	    
	    //git diff tag1 tag2 --name-only    所输出的log日志，直接copy过来就好
	    public static String changeLogStr = 
	    		"src/main/java/com/fzs/flow/http/action/admin/FlowTransactionAction.java\n" + 
	    		"src/main/java/com/fzs/flow/bean/FlowChannel.java\n" + 
	    		"src/main/java/com/fzs/flow/dao/FlowTransactionDao.java\n" + 
	    		"src/main/java/com/fzs/flow/service/impl/FlowChargeServiceImpl.java\n" + 
	    		"src/main/resources/conf/sql/FlowChannelDao.xml\n" + 
	    		"src/main/resources/conf/sql/FlowTransactionDao.xml\n" + 
	    		"src/main/webapp/WEB-INF/jsp/admin/channel/subChannelEdit.jsp\n" + 
	    		"src/main/webapp/resources/js/admin/channel/subChannelConf.js\n" +
	    		"src/main/filters/dev/deployment.properties\n" + 
	    		"src/main/filters/product/deployment.properties\n" + 
	    		"src/main/filters/test/deployment.properties\n" + 
	    		"src/main/java/com/fzs/flow/bean/Flow2ProductServ.java\n" + 
	    		"src/main/java/com/fzs/flow/http/action/admin/Flow2SourceProdcutAction.java\n" + 
	    		"src/main/java/com/fzs/flow/quartz/ChannelDiscountChangeQuartz.java\n" + 
	    		"src/main/java/com/fzs/flow/trade/pub/shijilong/SJLProviderHandler.java\n" + 
	    		"src/main/resources/conf/properties/deployment.properties\n"+
	    		"src/main/java/com/fzs/flow/channel/handler/BasicAPICallBackChannelHandler.java\n" + 
			"src/main/java/com/fzs/flow/http/action/admin/FlowTransactionAction.java\n" + 
			"src/main/java/com/fzs/flow/http/action/api/FlowExternalApiAction.java\n" + 
			"src/main/java/com/fzs/flow/http/callback/FlowAccessCallbackAction.java\n" + 
			"src/main/java/com/fzs/flow/modules/callback/controller/FlowAccessCallbackController.java\n" + 
			"src/main/java/com/fzs/flow/modules/recharge/controller/FlowExternalApiController.java\n" + 
			"src/main/resources/conf/message/dynamic-config.properties\n" + 
			"src/main/resources/conf/rabbitmq/springRabbitMqConf.xml\n" + 
			"src/main/resources/log4j.properties\n";
	    
	    public static int countFiles = 0;// 声明统计文件个数的变量
	    
	    public static int countFolders = 0;// 声明统计文件夹的变量
	    
	    //将补丁文件路径转为List：读取Str的方式
	    public static List<String> getPatchFileList(String changeLogStr) throws Exception{  
	        String str[] = changeLogStr.split("\n");
	        return Arrays.asList(str);
	    }  
	    
	    //将补丁文件路径转为List：读取TXT方式
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
	    
	    //复制文件
	    public static void copyFiles(List<String> list){ 
	    		System.out.println("changelog：git diff tag1 tag2 --name-only ");
	    		System.out.println("********************************************************");
	    		System.out.println(changeLogStr);
			System.out.println("********************************************************");
	        
			List<String> notFoundFiles = new ArrayList<String>();
			
	        for(String fullFileName:list){  
	            if(fullFileName.indexOf("src/main/java/")!=-1&&fullFileName.endsWith("java")){     //处理Class文件 
	                String fileName=fullFileName.replace("src/main/java","");  
	                fullFileName=classPath+fileName;  
	                if(fileName.endsWith(".java")){  
	                    fileName=fileName.replace(".java",".class");  
	                    fullFileName=fullFileName.replace(".java",".class");  
	                }  
	               
	                String classFileFolder = fullFileName.substring(0,fullFileName.lastIndexOf("/"));  //Class文件所在目录
	                String keyWord = fullFileName.substring(fullFileName.lastIndexOf("/")+1,fullFileName.length()).replaceAll(".class", "");  //查找的关键字：文件名
	                List<String> innerClassPath = getInnerClassPath(classFileFolder, keyWord);
	               
	                if(innerClassPath.size()>1){//有内部类
	                		foundInnerClassFileCount = foundInnerClassFileCount+1;
	                		for (String string : innerClassPath) {
	                			String desFileFolderStr=desPath+"/"+version+"/WEB-INF/classes";   //文件输出路径
	                			String innnerClassName = string.substring(string.lastIndexOf("/"),string.length()); //内部类名称
	                			String innerClassOutputFolder = desFileFolderStr+fileName.substring(0,fileName.lastIndexOf("/"))+innnerClassName;  //内部类输出位置，包含内部类文件名
							File desFilePath=new File(desFileFolderStr+fileName.substring(0,fileName.lastIndexOf("/")));  
							if(!desFilePath.exists()){  
							    desFilePath.mkdirs();  
							}
	                			copyFile(string, innerClassOutputFolder);  
	                			//System.out.println(filesNums+1+string+"复制完成");
	                			copyFilesFullUrlLog.add(filesNums+1+string+"复制完成");
	                			filesNums = filesNums+1;
	                			innerClassFileCount = innerClassFileCount+1;
	                			innerClassFileUrl.add(string);
						}
	                }else {  //不存在内部类
	                		String tempDesPath=fileName.substring(0,fileName.lastIndexOf("/"));  
		                String desFilePathStr=desPath+"/"+version+"/WEB-INF/classes"+tempDesPath;  
		                String desFileNameStr=desPath+"/"+version+"/WEB-INF/classes"+fileName;  
		                File desFilePath=new File(desFilePathStr);  
		                if(!desFilePath.exists()){  
		                    desFilePath.mkdirs();  
		                }
	                		copyFile(fullFileName, desFileNameStr);  
	                		copyFilesFullUrlLog.add(filesNums+1+fullFileName+"复制完成");
	                		filesNums = filesNums+1;
	                }
	                 
	            }else if(fullFileName.contains("src/main/webapp/")) { //处理JSP、JS文件
		            	String resourceFullPath = null; //将要复制文件全路径
	            		String desFullPath = null;      //复制文件后需要放置路径
	            	
		            	if(fullFileName.endsWith("js")) {
		            		String desFileName=fullFileName.substring(fullFileName.indexOf("/resources/"),fullFileName.length());
		            		resourceFullPath = projectPath+"/"+fullFileName;//将要复制的文件全路径 
		            		desFullPath = desPath+"/"+version+desFileName;  
		            		File desFilePath=new File(desFullPath.substring(0,desFullPath.lastIndexOf("/")));  
	 	                if(!desFilePath.exists()){  
	 	                    desFilePath.mkdirs();  
	 	                }
		            		copyFile(resourceFullPath, desFullPath);  
		            		copyFilesFullUrlLog.add(filesNums+1+resourceFullPath+"复制完成");
		            		filesNums = filesNums+1;
		            		
	            		}else if(fullFileName.endsWith(".jsp")) {
	            			
		            		String desFileName=fullFileName.substring(fullFileName.indexOf("/WEB-INF/"),fullFileName.length());
		            		resourceFullPath = projectPath+"/"+fullFileName;//将要复制的文件全路径 
		            		desFullPath = desPath+"/"+version+desFileName;  
		            		File desFilePath=new File(desFullPath.substring(0,desFullPath.lastIndexOf("/")));  
			                if(!desFilePath.exists()){  
			                    desFilePath.mkdirs();  
			                }
		            		copyFile(resourceFullPath, desFullPath);  
		            		copyFilesFullUrlLog.add(filesNums+1+resourceFullPath+"复制完成");
		            		filesNums = filesNums+1;
	            		}
	            }else if(fullFileName.indexOf("src/main/resources/conf/")!=-1){  //处理conf目录下的文件
	                String desFileName=fullFileName.substring(fullFileName.lastIndexOf("/resources/"),fullFileName.length());  
	                desFileName = desFileName.replaceAll("/resources", "");
	                fullFileName=projectPath+"/"+fullFileName;//将要复制的文件全路径  
	                String fullDesFileNameStr=desPath+"/"+version+"/WEB-INF/classes"+desFileName;  
	                String desFilePathStr=fullDesFileNameStr.substring(0,fullDesFileNameStr.lastIndexOf("/"));  
	                File desFilePath=new File(desFilePathStr);  
	                if(!desFilePath.exists()){  
	                    desFilePath.mkdirs();  
	                }  
	                copyFile(fullFileName, fullDesFileNameStr);  
	                copyFilesFullUrlLog.add(filesNums+1+fullDesFileNameStr+"复制完成");
	                filesNums = filesNums+1;
	            }else if(fullFileName.substring(0,fullFileName.lastIndexOf("/")).indexOf("src/main/resources")!=-1){  //处理其他放在/WEB-INF/classes/  目录下的配置文件 例如log、spring
	            		String desFileName=fullFileName.substring(fullFileName.lastIndexOf("src/main/resources"),fullFileName.length());  
					desFileName = desFileName.replaceAll("src/main/resources", "");
					fullFileName=projectPath+"/"+fullFileName;//将要复制的文件全路径  
					String fullDesFileNameStr=desPath+"/"+version+"/WEB-INF/classes"+desFileName;  
					String desFilePathStr=fullDesFileNameStr.substring(0,fullDesFileNameStr.lastIndexOf("/"));  
					File desFilePath=new File(desFilePathStr);  
					if(!desFilePath.exists()){  
					    desFilePath.mkdirs();  
					}  
					copyFile(fullFileName, fullDesFileNameStr);  
					copyFilesFullUrlLog.add(filesNums+1+fullDesFileNameStr+"复制完成");
					filesNums = filesNums+1;
				}else {  //记录没有匹配的项目
					notFoundFiles.add(fullFileName);
				}
	        }  
	        
	        System.out.println("复制文件操作记录：");
	        for (String copyFilesFullUrl : copyFilesFullUrlLog) {
        			System.out.println(copyFilesFullUrl);
	        }
	        System.out.println("##############################################");
	        System.out.println("待处理文件数目【changeLog】 ："+preFileCount);
	        int sum = (preFileCount-notFoundFiles.size())+(innerClassFileCount-foundInnerClassFileCount);
	        System.out.println("校验与【共复制文件数目（包含内部类）】是否相等（待处理文件数目 - 未能成功匹配的文件数）+（内部类总数（包含源文件）-发现内部类次数）= 【"+sum+"】");
	        if(sum!=filesNums) {
	        		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	        		System.out.println("@@@@@@@@@出错了！！！！！@@@@@@@@");
	        		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	        }
	        System.out.println("共复制文件数目（包含内部类） ："+filesNums);
	        System.out.println("发现内部类次数 ："+foundInnerClassFileCount);
	        System.out.println("内部类总数（包含源文件） ："+innerClassFileCount);
	        System.out.println("未能成功匹配的文件数目 ："+notFoundFiles.size());
	        String[] keys=notFoundFiles.toArray(new String[notFoundFiles.size()]);
	        for (String string : keys) {
	        		System.out.println(string);
			}
	        System.out.println("##############################################");
	    }  
	  
	    
	    /**
	     * 
	     * @param sourceFileNameStr  : 待复制的文件
	     * @param desFileNameStr     : 复制后的文件放置位置
	     */
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
	
	   
	 
	    public static File[] searchFile(File folder, final String keyWord) {// 递归查找包含关键字的文件
	 
	        File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
	            @Override
	            public boolean accept(File pathname) {// 实现FileFilter类的accept方法
	                if (pathname.isFile())// 如果是文件
	                    countFiles++;
	                else
	                    // 如果是目录
	                    countFolders++;
	                //if (pathname.isDirectory() || (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))// 目录或文件包含关键字:模糊查询
	                String pattern = keyWord.toLowerCase()+"+(\\$)?[0-9]*"; //匹配内部类的正则表达式：ChannelStatisticsAction$1.class ChannelStatisticsAction$2.class 
	                String className = pathname.getName().toLowerCase().replaceAll(".class", "");
	                boolean isMatch = Pattern.matches(pattern, className);
	                if (pathname.isDirectory() || (pathname.isFile() && isMatch))// 目录或文件包含关键字，精确查询，匹配内部类
	                    return true;
	                return false;
	            }
	        });
	 
	        List<File> result = new ArrayList<File>();// 声明一个集合
	        for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
	            if (subFolders[i].isFile()) {// 如果是文件则将文件添加到结果列表中
	                result.add(subFolders[i]);
	            } else {// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
	                File[] foldResult = searchFile(subFolders[i], keyWord);
	                for (int j = 0; j < foldResult.length; j++) {// 循环显示文件
	                    result.add(foldResult[j]);// 文件保存到集合中
	                }
	            }
	        }
	 
	        File files[] = new File[result.size()];// 声明文件数组，长度为集合的长度
	        result.toArray(files);// 集合数组化
	        return files;
	    }
	 
	    
	    /**
	     * 获取内部类方法
	     * @param fileFolder
	     * @param keyWord
	     */
	    public static List<String> getInnerClassPath(String classFileFolder,String keyWord) {
//	    		File folder = new File(classPath+"/"+"/com/fzs/flow/http/action/");// 默认目录
//	    		String keyword = "ChannelStatisticsAction";
	    		File folder = new File(classFileFolder);// 默认目录
	        String keyword = keyWord;

	        	List<String> innerClassPath = new ArrayList<String>();
	        if (!folder.exists()) {// 如果文件夹不存在
	            System.out.println("目录不存在：" + folder.getAbsolutePath());
	            return innerClassPath;
	        }
	        File[] result = searchFile(folder, keyword);// 调用方法获得文件数组
	        if(result.length>1) {//有内部类的时候才输出
	        	 	System.out.println("---------------------------------------------------------------------------------------------------------");
	        		System.out.println(keyword+"【发现内部类】");
	        		System.out.println("在 " + folder + " 以及所有子文件时查找对象：" + keyword);
	        		System.out.println("查找了" + countFiles + " 个文件，" + countFolders + " 个文件夹，共找到 " + result.length + " 个符合条件的文件：");
        			for (int i = 0; i < result.length; i++) {// 循环显示文件
 	 	            File file = result[i];
 	 	            System.out.println(file.getAbsolutePath() + " ");// 显示文件绝对路径
 	 	            innerClassPath.add(file.getAbsolutePath());
 	 	        }
	 	        System.out.println("---------------------------------------------------------------------------------------------------------");
	        }
	        return innerClassPath;
	    }
	    
	    
	    
	    public static void main(String[] args) {// java程序的主入口处  
	    		//copyFiles(getPatchFileList());    //读取Txt的方式
	        try {
	        		int listSize = getPatchFileList(changeLogStr).size();
	        		preFileCount = listSize;
	        		System.out.println("提交后改变文件数是：【"+preFileCount+"】");
				copyFiles(getPatchFileList(changeLogStr));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   //读取Str的方式
	    }
	    
}
