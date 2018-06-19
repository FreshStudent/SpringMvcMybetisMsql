package com.myprojct.ssm.modules.jdbcBatchInsert;


/**
 * 2018-06-06
 * 原来插入数据库中的处理方式是依次打开连接，在关闭再打开，然后释放连接池。
 * 改进版本是，直接批量处理数据，将信息储蓄到一定数量的时候在释放，这样的话 只打开较少的连接池次数，从而节省时间。
 * 
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;

import com.google.common.collect.Lists;

public class InsertDataOracle {

	public static void main(String[] args) throws IOException {
		ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();// 单例模式创建连接池对
		List<String> phones = readFile("src\\mobile.txt");
		List<List<String>> pushsArrays = Lists.partition(phones, 1919);// 拆分
		CompletionService<List<String>> completionService = PubThreadPoolExecutor.getInstance().getCompletionService();
		for (final List<String> pushsArray : pushsArrays) {
			completionService.submit(new Callable<List<String>>() {
				public List<String> call() throws Exception {
					try {
						Connection conn = null;
						PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
						conn = connPool.getConnection();// 创建一个数据库连接
						//String sql = "INSERT INTO T_CHECK_MOBILE_INFO1 (MOBILE) VALUES (?)";
						String sql = "";
						pre = conn.prepareStatement(sql);// 实例化预编译语句
						try {
							for (String mobile : pushsArray) {
								pre.setString(1, mobile); // 参数1
								pre.addBatch();           //添加批量处理
								System.out.println(mobile + "-成功！");
							}
							pre.executeBatch();  //批量执行
						} finally {

							if (pre != null)
								pre.close();
							if (conn != null)
								connPool.returnConnection(conn);// 连接使用完后释放连接到连接池
							System.out.println("连接使用完后释放连接到连接池！");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});
		}
	}

	public static List<String> readFile(String path) throws IOException {
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("要读取的文件不存在");
		}
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		isr.close();
		fis.close();
		return list;
	}
}
