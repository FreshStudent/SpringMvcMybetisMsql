package com.myprojct.ssm.dao;

import com.myprojct.ssm.bean.tcode;

public interface tcodeMapper {
    int insert(tcode record);

    int insertSelective(tcode record);
}