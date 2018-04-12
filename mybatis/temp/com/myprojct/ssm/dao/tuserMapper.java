package com.myprojct.ssm.dao;

import com.myprojct.ssm.bean.tuser;

public interface tuserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(tuser record);

    int insertSelective(tuser record);

    tuser selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(tuser record);

    int updateByPrimaryKey(tuser record);
}