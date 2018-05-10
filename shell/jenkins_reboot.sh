#!/bin/bash
#defined

#1.BASE_SETTING

#TOMCAT_INFO
# TOMCAT_HOME = "/u01/samp/soft/apache/tomcats/tomcat-flow-19008/"
# TOMCAT_PORT = "19008"
# TOMCAT_NAME = "tomcat-flow-19008"
# PROJECT_NAME="fzsFlow"

TOMCAT_HOME="/Users/liquanliang/Documents/AboutDev/Tomcat/apacheTomcat8.5.11"
TOMCAT_PORT="8080"
TOMCAT_NAME="tomcat"
PROJECT_NAME="fzsFlow"
TOMCAT_WEBAPP_URL="webapps"

## 时间戳 eg：20180510142558
DATE_TIME=$(date +%Y%m%d%H%M%S)

## 备份文件夹名称
BACKUP_FOLDER_NAME="backupWarPackages"


# ## 创建备份文件夹（若没有则创建） begin
# echo 创建备份文件夹（若没有则创建） beginning.......
# if [ -d "$TOMCAT_HOME/$BACKUP_FOLDER_NAME" ] ; then  
# echo "文件夹存在！"  
# else  
# echo "文件夹不存在,创建文件夹：$BACKUP_FOLDER_NAME"  
# cd $TOMCAT_HOME
# mkdir $BACKUP_FOLDER_NAME
# fi  
# echo 创建备份文件夹（若没有则创建） end.......
# # 创建备份文件夹 end

# ## 备份文件
# echo 备份服务器原来文件夹 beginning.......
# ## 先备份原war解压后的folder
# echo 先备份原来的war解压后的folder
# cd $TOMCAT_HOME/$TOMCAT_WEBAPP_URL
# # zip -r $TOMCAT_HOME/$BACKUP_FOLDER_NAME/"$PROJECT_NAME$DATE_TIME.zip" $PROJECT_NAME
# echo 查看备份目录下的zip文件：
# ls -lt $TOMCAT_HOME/$BACKUP_FOLDER_NAME 
# echo 备份服务器原来文件夹 end.......


# ## 删除webapps下的war包和war包解压后的文件夹
# echo 删除webapps下的war包和war包解压后的文件夹  beginning......
# cd $TOMCAT_HOME/$TOMCAT_WEBAPP_URL 
# rm -rf "$PROJECT_NAME".war
# rm -rf "$PROJECT_NAME"
# echo 删除webapps下的war包和war包解压后的文件夹  end......




## Kill 所有运行中的Tomcat begin
echo 找出所有的Tomcat pid 并kill掉  beginning........


TOMCAT_PID=$(ps -ef|grep $TOMCAT_NAME|grep start|grep -v 'grep'|awk '{print $2}')
#遍历是否存在其他tomcat，如果有的话，kill掉
while [ -n "$TOMCAT_PID" ]    
	do 
		echo "scan tomcat pid :" $TOMCAT_PID
	    sh $TOMCAT_HOME/bin/shutdown.sh
	    sleep 3s
	    kill -9 $TOMCAT_PID
		echo $TOMCAT_PID
		sleep 3s
		TOMCAT_PID=$(ps -ef|grep $TOMCAT_NAME|grep start|grep -v 'grep'|awk '{print $2}')  ##再查找一次tomcat的pid
done
echo 找出所有的Tomcat pid 并kill掉  end........
## Kill 所有运行中的Tomcat end


