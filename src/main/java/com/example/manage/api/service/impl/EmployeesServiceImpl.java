package com.example.manage.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manage.api.dao.EmployeesDao;
import com.example.manage.api.entity.EmployeesEntity;
import com.example.manage.api.service.EmployeesService;
import org.springframework.stereotype.Service;


@Service("employeesService")
public class EmployeesServiceImpl extends ServiceImpl<EmployeesDao, EmployeesEntity> implements EmployeesService {


}
