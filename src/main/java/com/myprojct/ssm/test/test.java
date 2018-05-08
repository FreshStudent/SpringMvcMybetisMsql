package com.myprojct.ssm.test;


public class test {

		public static void main(String[] args) {
			
//			src/main/resources/conf/sql/FlowChannelDao.xml
//			src/main/resources/conf/sql/FlowTransactionDao.xml
//			src/main/webapp/WEB-INF/jsp/admin/channel/subChannelEdit.jsp
//			src/main/webapp/resources/js/admin/channel/subChannelConf.js
//			String fullFileName = "src/main/resources/conf/sql/FlowChannelDao.xml";
			String fullFileName = "src/main/resources/conf/sql/FlowTransactionDao.xml";
			
			String desFileName=fullFileName.substring(fullFileName.indexOf("/resources/"),fullFileName.length());
		  	System.out.println("desFileName:"+desFileName);
		}
		
	}
	
