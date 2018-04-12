package com.myprojct.ssm.dao;

import com.myprojct.ssm.bean.User;
/**
 * DAO接口层
 * @author 
 * @time 2015.5.15
 */
public interface UserDao {
	/**
	 * 根据用户ID查询用户信息
	 * @param id
	 * @return
	 */
	public User findUserById(int id);
}

