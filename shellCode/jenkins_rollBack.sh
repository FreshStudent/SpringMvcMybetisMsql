#!/bin/bash
#defined

#TOMCAT_INFO
TOMCAT_HOME="/u01/samp/soft/apache/tomcats/tomcat-flow-19008"
TOMCAT_PORT="19008"
TOMCAT_NAME="tomcat-flow-19008"
PROJECT_NAME="fzsFlow"
TOMCAT_WEBAPP_URL="webapps"

TOMCAT_SHUTDOWN=$TOMCAT_HOME/bin/shutdown.sh  
TOMCA_START=$TOMCAT_HOME/bin/startup.sh

## 时间戳 eg：20180510142558
DATE_TIME=$(date +%Y%m%d%H%M%S)

## 备份文件夹名称
BACKUP_FOLDER_NAME="backupWarPackages"


# ## 遍历备份文件夹中的zip文件，根据创建时间倒序排列，获取第一个
ZIP_NAME=$(ls -l -r $TOMCAT_HOME/$BACKUP_FOLDER_NAME | grep -v 'total' |head -n 1 | awk '{print $9}')

if [ ! -f $TOMCAT_HOME/$BACKUP_FOLDER_NAME/$ZIP_NAME ];
then
	echo "文件不存在，请检查服务器！"
else
	echo 本次回滚的zip是：$ZIP_NAME
	sleep 3s
	## Kill 所有运行中的Tomcat begin
	echo ------------------------------------------找出所有的Tomcat pid 并kill掉  beginning------------------------------------------
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
	echo ------------------------------------------找出所有的Tomcat pid 并kill掉  end---------------------------------------
	## Kill 所有运行中的Tomcat end

	## 删除webapps下的war包和war包解压后的文件夹
	echo ------------------------------------------删除webapps下的war包和war包解压后的文件夹  beginning------------------------
	rm -rf $TOMCAT_HOME/$TOMCAT_WEBAPP_URL/$PROJECT_NAME.war
	rm -rf $TOMCAT_HOME/$TOMCAT_WEBAPP_URL/$PROJECT_NAME
	echo ------------------------------------------删除webapps下的war包和war包解压后的文件夹  end------------------------------

	## 解压备份后的zip包到webapps目录
	echo ------------------------------------------复制war包到webapps目录 begin---------------------------------------------
	unzip $TOMCAT_HOME/$BACKUP_FOLDER_NAME/$ZIP_NAME -d $TOMCAT_HOME/$TOMCAT_WEBAPP_URL
	echo ------------------------------------------复制war包到webapps目录 end-----------------------------------------------

	sleep 3s
	## 启动tomcat
	echo ------------------------------------------启动Tomcat begin-------------------------------------------------------
	$TOMCA_START
	echo ------------------------------------------启动Tomcat end---------------------------------------------------------
fi

