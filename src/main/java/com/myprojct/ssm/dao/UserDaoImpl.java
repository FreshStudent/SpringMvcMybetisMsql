package com.myprojct.ssm.dao;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;  
import  com.myprojct.ssm.mapper.UserMapper;  
import com.myprojct.ssm.bean.User;  
/** 
 * DAO实现层 
 * @author  
 * @time 2015.5.15 
 */  
@Component  
public class UserDaoImpl implements UserDao {  
    @Autowired  
    private UserMapper userMapper;  

    public User findUserById(int id) {  
        User user = userMapper.selectUserById(id);  
         return user;   
    }  
      
}  