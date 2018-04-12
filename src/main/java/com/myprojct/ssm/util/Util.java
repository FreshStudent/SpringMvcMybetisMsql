package com.myprojct.ssm.util;

import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;

public class Util {
	private static final String RANDOM_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected final static SimpleDateFormat sdfTrans = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public static String getOrderNo(){
		Date date = new Date();
		String dt = sdfTrans.format(date);
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(6);
		dt=dt+randomAlphanumeric;
		return dt;
	}
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String sourceIP = "";
		if (Util.isNotEmpty(ip)) {
			String[] split = ip.split(",");
			if (Util.isNull(split)) {
				sourceIP = ip;
			} else {
				sourceIP = split[0].trim();
			}
		}
		return sourceIP;
	}
	public static SimpleDateFormat getSimpleDateFormat(String flag){
		if(flag.equalsIgnoreCase("month")){
			return new SimpleDateFormat("yyyy-MM");
		}else if(flag.equalsIgnoreCase("day")){
			return new SimpleDateFormat("yyyy-MM-dd");
		}else if(flag.equalsIgnoreCase("hour")){
			return new SimpleDateFormat("yyyy-MM-dd HH");
		}else if(flag.equalsIgnoreCase("minute")){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}else{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	}
	public static SimpleDateFormat getLinkSimpleDateFormat(String flag){
		if(flag.equalsIgnoreCase("month")){
			return new SimpleDateFormat("yyyyMM");
		}else if(flag.equalsIgnoreCase("day")){
			return new SimpleDateFormat("yyyyMMdd");
		}else if(flag.equalsIgnoreCase("hour")){
			return new SimpleDateFormat("yyyyMMddHH");
		}else if(flag.equalsIgnoreCase("minute")){
			return new SimpleDateFormat("yyyyMMddHHmm");
		}else if(flag.equalsIgnoreCase("seconds")){
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}else{
			return new SimpleDateFormat("yyyyMMddHHmmssSSS");
		}
	}
	
	public static String getCurrentDay() {
		SimpleDateFormat countDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar curCalendar = Calendar.getInstance();
		String curDate = countDateFormat.format(curCalendar.getTime());
		return curDate;
	}
	
	public static String getCurrentDay(String flag,int vetor) {
		SimpleDateFormat countDateFormat = new SimpleDateFormat(flag);
		Calendar curCalendar = Calendar.getInstance();
		curCalendar.add(Calendar.DATE,vetor);
		String curDate = countDateFormat.format(curCalendar.getTime());
		return curDate;
	}
	
	public static int  computeStartPage(int page,int rows){
		int begin=(page-1)*rows+1;
		return begin;
	}
	public static int  computeEndPage(int page,int rows){
		int nmb=page*rows;
		return nmb;
	}
	
	public static String encryptMD5GBK(String paramString) {
		char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] arrayOfByte ;
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(	new String (paramString.getBytes("UTF-8"),"GBK").getBytes());
		
			arrayOfByte = localMessageDigest.digest();
			int i = arrayOfByte.length;
			char[] arrayOfChar2 = new char[i * 2];
			int j = 0;
			for (int k = 0; k < arrayOfByte.length; k++) {
				int m = arrayOfByte[k];
				arrayOfChar2[(j++)] = arrayOfChar1[(m >>> 4 & 0xF)];
				arrayOfChar2[(j++)] = arrayOfChar1[(m & 0xF)];
			}
			return new String(arrayOfChar2);
		} catch (Exception localException) {
		}
		return null;
	}
	/**
	 * md5加密
	 * @param paramString
	 * @return
	 */
	public static String encryptMD5(String paramString) {
		char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] arrayOfByte = paramString.getBytes();
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(arrayOfByte);
			arrayOfByte = localMessageDigest.digest();
			int i = arrayOfByte.length;
			char[] arrayOfChar2 = new char[i * 2];
			int j = 0;
			for (int k = 0; k < arrayOfByte.length; k++) {
				int m = arrayOfByte[k];
				arrayOfChar2[(j++)] = arrayOfChar1[(m >>> 4 & 0xF)];
				arrayOfChar2[(j++)] = arrayOfChar1[(m & 0xF)];
			}
			return new String(arrayOfChar2);
		} catch (Exception localException) {
		}
		return null;
	}
	/**
	 * 包含大小写字母，数字的随机字符串
	 * @param len
	 * @return
	 */
	public static String random(int len) {
		String random = "";
		for (int i = 0; i < len; i++) {
			double rd = Math.random() * 61;
			long seed = Math.round(rd);
			if (seed > 61) {
				seed = 61;
			}
			if (seed < 0) {
				seed = 0;
			}
			String seedStr = String.valueOf(seed);
			int num;
			try {
				num = Integer.parseInt(seedStr);
			} catch (Exception e) {
				num = 0;
			}
			random += String.valueOf(RANDOM_CHAR.charAt(num));
		}
		return random;
	}
	
	/**
	 * 生成不重复的随机数
	 * @param len
	 * @return
	 */
	public  static  String genarateRandom(List<String> list,int len) {
		
		 //生成n位随机验证码
		 StringBuffer random = null ; 
		 Random rand =new Random(25);
		 while(true){
			  random = new StringBuffer("");
			  for(int i=0;i<len;i++){
//			    	random.append(Math.round(Math.random() * 10));
					random.append(rand.nextInt(10));
		      }
	          if(!list.contains(random.toString())){
	        	   break;	        	   
	          }
	          
		 }
	   
		return random.toString();
	}
	
	
	public static boolean isEmpty(Collection<?> c){
		if(isNull(c)){
			return true;
		}
		return c.size()<1?true:false;
	}
	public static boolean isEmpty(String s){
		if(isNull(s)){
			return true;
		}
		return s.length()<1?true:false;
	}
	public static boolean isEmpty(Object[] s){
		if(isNull(s)){
			return true;
		}
		return s.length<1?true:false;
	}
	
	public static boolean isNotEmpty(Collection<?> c){
		return !isEmpty(c);
	}
	public static boolean isNotEmpty(String s){
		return !isEmpty(s);
	}
	public static boolean isNotEmpty(Object[] s){
		return !isEmpty(s);
	}
	
	public static boolean isNull(Object obj){
		return obj==null?true:false;
	}
	public static boolean isNotNull(Object obj){
		return !isNull(obj);
	}
	public static boolean isEqual(String s1, String s2) {
		if (isEmpty(s1) && isEmpty(s2)) {
			return true;
		}
		return s1.equals(s2);
	}
	public static boolean isNotEqual(String s1, String s2) {
		return !isEqual(s1, s2);
	}
	public static String  getRandom(int len){
		UUID uuid = UUID.randomUUID();    
	 String str = uuid.toString().replace("-", "");
	  			
		return str.substring(0, len>str.length()?str.length():len) ;
	  }
	
		/**
		 * numberFormat(2,5 )  return: 00002
		 * @param number
		 * @param pos
		 * @return
		 */
	public static String numberFormat(int number,int addpos ){
		 NumberFormat formatter = NumberFormat.getNumberInstance();   
		 formatter.setMinimumIntegerDigits(addpos);   
		 formatter.setGroupingUsed(false);   
		 String s = formatter.format(number);   
		 return s;
	}
	
	
	 public static byte[] subBytes(byte[] src, int begin, int count) {
	        byte[] bs = new byte[count];
	        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
	        return bs;
	    }
	
	public static void main(String[] args) {
		 System.out.println(encryptMD5("fzspay890"));
	}
	
	
}
