package com.myprojct.ssm.test;


public class test {

		public static void main(String[] args) {
			
			String contactNumsStr = "13809247889";
			String[] strArray = null;   
	        strArray = contactNumsStr.split(","); //拆分字符为"," ,然后把结果交给数组strArray 
			for (int i=0;i<strArray.length;i++) {
				System.out.println("sb:"+strArray[i]);
			}
		}
		
	}
	
