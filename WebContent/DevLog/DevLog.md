#2017-8-11 记录开发中log
####2017-8-11
- About
    - 导入spring+mybetis的jar包
    - 建立数据表，并生成相关的sql语句
    - 搭建了SpringMvc跳转
- Next
    - 通过boostrap来实现前台页面
***
####2017-8-11
- About
    - 导入spring+mybetis的jar包
    - 建立数据表，并生成相关的sql语句
    - 搭建了SpringMvc跳转
- Next
    - 现在做到导入mapper文件这一块
    - 通过boostrap来实现前台页面
    
***
####2017-08-12
- About
    - Mac下载Win上传到github的代码，注意：需要把项目转为Web项目
    - 实现了SpringMvc+mybetis+mysql的整合，并且实现了页面访问数据库内容并能正确显示
    - web.xml、Spring*.xml配置文件中的classpath指向问题，需要在Java Resources中写入xml配置文件，这样写classpath才会有效，才能查找到对应的配置文件。
- Next
    - 想要实现一个系统，相关的CRUD，但是暂时没确定做什么项目比较好？
    - 相关UML建模、SQL数据库之间建模工具需要选择，目的是为了说明白做了什么项目，及里面的相关架构。

***
####2017-08-13
- About
    - 利用spring的cron实现了定时任务，基于cron的注解方式，不用在xml中配置，相对来说比较方便，相关的demo是DemoTask。
- Next
    - 配置log日志输出
    - 实现事务管理，配置AOP事务管理，用于sql插入失败时候回滚。
    - 做一个MVC分层，建立相关：baseController、baseService、baseDao、Util(用面向接口方式来实现，interface——》interfaceImpl)
    - 逆向生成表及pojo+mapper+dao+dao.xml
***

####2017-08-14
- About
    - 实现了Mybetis逆向导出Mysql的表，生成了javabean、sqlXml、MapperXml文件。
    - 步骤：
    - 一、配置generatorTableConfig.xml
	- 1、相关数据库连接jar包。
	- 2、相关数据库的driverenClass、JdbcUrl、userid、password配置。
	- 3、相关的javabean、sqlXml、mapperXml的生成路径。
	- 4、配置表名或者相关的相关的别名，可以配置多个表。
	- 二、运行genTable.java中的main方法。
- Next
    - 配置log日志输出
    - 实现事务管理，配置AOP事务管理，用于sql插入失败时候回滚。
    - 做一个MVC分层，建立相关：baseController、baseService、baseDao、Util(用面向接口方式来实现，interface——》interfaceImpl)
    
    
    
