package com.myprojct.ssm.modules.jdbcBatchInsert;

/* 连接池工具类，返回唯一的一个数据库连接池对象,单例模式 */
public class ConnectionPoolUtils {
    private ConnectionPoolUtils() {};// 私有静态方法

    private static ConnectionPool poolInstance = null;

    private static String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=10.20.1.21)(HOST=10.20.1.22)(PORT=1521))(FAILOVER=ON)(LOAD_BALANCE=ON)(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=fzsdb)))";

    private static String user = "uFlowSystem_dev";

    private static String pwd = "lxkj";
    /*
    private static String url = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.20.100.63)(PORT = 1521))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = fzsdb)))";

    private static String user = "unewflow_test";

    private static String pwd = "fzs123456";
    */
    public static ConnectionPool GetPoolInstance() {
        if (poolInstance == null) {
            poolInstance = new ConnectionPool("oracle.jdbc.driver.OracleDriver", url, user, pwd);
            try {
                poolInstance.createPool();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return poolInstance;
    }
}
