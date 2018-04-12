package com.myprojct.ssm.bean;

/**
 * User表映射类
 * @author fox
 *
 */
public class User {

    private Integer userId;  //用户ID
    
    private String userName;  //用户名
    
    private int userAge;     //用户年龄
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }
    
    @Override  
    public String toString() {  
        return "User [userId=" + userId + ", userName=" + userName  
                + ", userAge=" + userAge + "]";  
    }  
    
}
