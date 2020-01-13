package com.example.manage.api.dao;

import com.example.manage.api.entity.EmployeesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author: cjl
 *
 * @date 2019-12-27 10:12:51
 */
@Mapper
public interface EmployeesDao extends BaseMapper<EmployeesEntity> {
	
}
