package com.example.manage.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author zhy
 * @email 2434017367@qq.com
 * @date 2019-12-24 19:48:56
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
