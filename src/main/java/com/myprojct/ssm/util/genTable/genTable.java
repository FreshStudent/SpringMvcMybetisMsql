package com.myprojct.ssm.util.genTable;

/**
 * 执行main函数，读取generatorTableConfig.xml文件来实现逆向导出数据表
 * @author liquanliang
 *
 */


/**
 * 
 * 步骤：
 * 一、配置generatorTableConfig.xml
 *	  1、相关数据库连接jar包。
 *    2、相关数据库的driverenClass、JdbcUrl、userid、password配置。
 *    3、相关的javabean、sqlXml、mapperXml的生成路径。
 *    4、配置表名或者相关的相关的别名，可以配置多个表。
 * 
 * 二、运行genTable.java中的main方法。
 */

public class genTable extends AbstractGen
{

    @Override
    public String getConfPath()
    {
        return "mybatis/generatorTableConfig.xml";//读取逆向生成数据表的配置文件
    }
    
    public static void main(String[] args)
    {
        AbstractGen gen = new genTable();
        gen.gen();
        //ceshi
    }
}
