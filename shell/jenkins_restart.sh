#!/bin/bash
#defined

#TOMCAT_INFO
TOMCAT_HOME="/u01/samp/soft/apache/tomcats/tomcat-flow-19008"
TOMCAT_PORT="19008"
TOMCAT_NAME="tomcat-flow-19008"
TOMCAT_WEBAPPS_NAME="webapps"
TOMCAT_SHUTDOWN=$TOMCAT_HOME/bin/shutdown.sh  
TOMCA_START=$TOMCAT_HOME/bin/startup.sh

PROJECT_NAME="fzsFlow"

##临时上传的war包名称
TEMP_UPLOAD_WAR_NAME="tempUploadWar"

## 时间戳 eg：20180510142558
DATE_TIME=$(date +%Y%m%d%H%M%S)

## 备份文件夹名称，备份服务器中正在运行的folder
BACKUP_RUNNING_WAR_FOLDER_NAME="backupRunningWar"

## 创建备份文件夹（若没有则创建） begin
echo ---------------------------备份文件夹$TEMP_UPLOAD_WAR_NAME，备份临时上传的war（若没有则创建） beginning
if [ -d "$TOMCAT_HOME/$TEMP_UPLOAD_WAR_NAME" ] ; then  
echo "文件夹存在！"  
else  
echo "文件夹不存在,创建文件夹：$TEMP_UPLOAD_WAR_NAME"  
cd $TOMCAT_HOME
mkdir $TEMP_UPLOAD_WAR_NAME
fi  
echo ---------------------------备份文件夹$TEMP_UPLOAD_WAR_NAME，备份临时上传的war（若没有则创建） end

echo ---------------------------备份文件夹$BACKUP_RUNNING_WAR_FOLDER_NAME，备份服务器中正在运行的folder（若没有则创建） beginning
if [ -d "$TOMCAT_HOME/$BACKUP_RUNNING_WAR_FOLDER_NAME" ] ; then  
echo "文件夹存在！"  
else  
echo "文件夹不存在,创建文件夹：$BACKUP_RUNNING_WAR_FOLDER_NAME"  
cd $TOMCAT_HOME
mkdir $BACKUP_RUNNING_WAR_FOLDER_NAME
fi  
echo ---------------------------备份文件夹$BACKUP_RUNNING_WAR_FOLDER_NAME，备份服务器中正在运行的folder（若没有则创建） end
# 创建备份文件夹 end

## 备份文件
echo ---------------------------备份服务器原来文件夹 beginning
## 先备份原war解压后的folder
cd $TOMCAT_HOME/$TOMCAT_WEBAPPS_NAME
zip -r $TOMCAT_HOME/$BACKUP_RUNNING_WAR_FOLDER_NAME/"$PROJECT_NAME$DATE_TIME.zip" $PROJECT_NAME
echo 查看备份目录下的zip文件：
ls -lt $TOMCAT_HOME/$BACKUP_RUNNING_WAR_FOLDER_NAME 
echo ---------------------------备份服务器原来文件夹 end

## Kill 所有运行中的Tomcat begin
echo ---------------------------找出所有的Tomcat pid 并kill掉  beginning
TOMCAT_PID=$(ps -ef|grep $TOMCAT_NAME|grep start|grep -v 'grep'|grep -v 'jenkins_restart.sh'|grep -v 'zip -r'|awk '{print $2}')
#遍历是否存在其他tomcat，如果有的话，kill掉
while [ -n "$TOMCAT_PID" ]    
	do 
		echo "scan tomcat pid :" $TOMCAT_PID
	    $TOMCAT_SHUTDOWN
	    sleep 3s
	    kill -9 $TOMCAT_PID
		sleep 3s
		TOMCAT_PID=$(ps -ef|grep $TOMCAT_NAME|grep start|grep -v 'grep'|grep -v 'jenkins_restart.sh'|grep -v 'zip -r'|awk '{print $2}')  ##再查找一次tomcat的pid
done
echo ---------------------------找出所有的Tomcat pid 并kill掉  end
## Kill 所有运行中的Tomcat end

## 删除webapps下的war包和war包解压后的文件夹
echo ---------------------------删除webapps下的war包和war包解压后的文件夹  beginning
rm -rf $TOMCAT_HOME/$TOMCAT_WEBAPPS_NAME/$PROJECT_NAME.war
rm -rf $TOMCAT_HOME/$TOMCAT_WEBAPPS_NAME/$PROJECT_NAME
echo ---------------------------删除webapps下的war包和war包解压后的文件夹  end

## 复制war包到webapps目录
echo ---------------------------复制war包到webapps目录 beginning
cp $TOMCAT_HOME/$TEMP_UPLOAD_WAR_NAME/$PROJECT_NAME.war $TOMCAT_HOME/$TOMCAT_WEBAPPS_NAME
echo ---------------------------复制war包到webapps目录 end

## 启动tomcat
echo ---------------------------启动Tomcat beginning
$TOMCA_START
echo ---------------------------启动Tomcat end

## 删除之前上传的临时war包
echo ---------------------------删除之前上传的临时war包 beginning
rm -rf $TOMCAT_HOME/$TEMP_UPLOAD_WAR_NAME/$PROJECT_NAME.war
echo ---------------------------删除之前上传的临时war包 end


