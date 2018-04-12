package com.myprojct.ssm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * 2017-08-17
 * 读取txt中文件，下划线和驼峰式命名的互相转换
 * @author liquanliang
 *
 */
public class ReadTxt2CamelCase {
	
	
	private static final char SEPARATOR = '_';
	private static final String UNDERLINENAME = "underlineName";
	private static final String CAMELCASE = "camelCase";
	
	 /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     * @param opt 操作 ：   underlineName-字符串转换为下划线    camelCase-下滑线转换为字符串  
     * 
     */
    public static void readTxtFileToCamelCase(String filePath,String opt){
        try {
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    
                    String lineTxt = null;   //当前行内容
                    
//                    System.out.println("转换前：");
//                    while((lineTxt = bufferedReader.readLine()) != null){
//                        System.out.println(lineTxt);
//                    }
                    
                    System.out.println("转换后：");
                    if("underlineName".equals(opt)) {
						while((lineTxt = bufferedReader.readLine()) != null){
							System.out.println(ReadTxt2CamelCase.toUnderlineName(lineTxt));             //user_id
							//System.out.println(lineTxt);
						}
                    }else if("camelCase".equals(opt)) {
						while((lineTxt = bufferedReader.readLine()) != null){
							System.out.println(ReadTxt2CamelCase.toCamelCase(lineTxt));    //isoCertifiedStaff
							//System.out.println(lineTxt);
						}
                    }
                    
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
     
    }
    
//    toUnderlineName("ISOCertifiedStaff"));  //iso_certified_staff
//    toUnderlineName("CertifiedStaff"));     //certified_staff
//    toUnderlineName("UserID"));             //user_id
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
 
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            boolean nextUpperCase = true;
 
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
 
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
 
            sb.append(Character.toLowerCase(c));
        }
 
        return sb.toString();
    }
 
//    toCamelCase("iso_certified_staff"));    //isoCertifiedStaff
//    toCamelCase("certified_staff"));        //certifiedStaff
//    toCamelCase("user_id"));				//userId
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
 
        s = s.toLowerCase();
 
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
 
        return sb.toString();
    }
 
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
 
    public static void main(String[] args) {
    	
//		System.out.println(ReadTxt2CamelCase.toUnderlineName("ISOCertifiedStaff"));  //iso_certified_staff
//		System.out.println(ReadTxt2CamelCase.toUnderlineName("CertifiedStaff"));     //certified_staff
//		System.out.println(ReadTxt2CamelCase.toUnderlineName("UserID"));             //user_id
//		System.out.println(ReadTxt2CamelCase.toCamelCase("iso_certified_staff"));    //isoCertifiedStaff
//		System.out.println(ReadTxt2CamelCase.toCamelCase("certified_staff"));        //certifiedStaff
//		System.out.println(ReadTxt2CamelCase.toCamelCase("user_id"));				//userId
    	
		String filePath = "//Users//liquanliang//Desktop//good.txt";
		//readTxtFileToCamelCase(filePath,ReadTxt2CamelCase.UNDERLINENAME);
		readTxtFileToCamelCase(filePath,ReadTxt2CamelCase.CAMELCASE);
        
    }
	
	

}
