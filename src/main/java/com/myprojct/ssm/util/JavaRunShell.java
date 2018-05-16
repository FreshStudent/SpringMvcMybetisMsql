package com.myprojct.ssm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JavaRunShell {
	public static void main(String[] args) {
		try {
			//获得执行Shell脚本执行权限
			String shpath = "/Users/liquanliang/git/FZS_FLOW_PLATFORM/update_pkg/getFileUR.sh";
			String command = "chmod 777 " + shpath;
			Process ps1 = Runtime.getRuntime().exec(command);
			ps1.waitFor();

			//执行shell脚本
			String command1 = "sh " + shpath;
			Process ps = Runtime.getRuntime().exec(command1);
			ps.waitFor();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			
			//输出shell执行结果，若脚本过于复杂的话，稍等片刻才会结果输出
			String result = sb.toString();
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
