package com.myprojct.ssm.redis;

import redis.clients.jedis.Jedis;

public class RedisHelloWorld {
	public static void main(String[] args) {
		try {
		             Jedis jr = new Jedis("127.0.0.1",6379); //redis服务地址和端口号  :6379
		             
		             String key1 = "key1";
		             String value1 = "hello Redis!";
		             jr.set(key1, value1);
		             
		             String key2 = "key2";
		             String value2 = "hello Redis2!";
		             jr.set(key2, value2);
		             
		             System.out.println(new String(jr.get(key1)));
		             System.out.println(new String(jr.get(key2)));
		        } catch (Exception e) {
		// TODO: handle exception
		        }
		    }
}
