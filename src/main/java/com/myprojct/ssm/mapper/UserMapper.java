package com.myprojct.ssm.mapper;

import com.myprojct.ssm.bean.User;
/**
 * Mapper映射类
 * @author 
 * @time 2015.5.15
 */
public interface UserMapper {
	
	public User selectUserById(int userId);

}
