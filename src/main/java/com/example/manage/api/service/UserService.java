package com.example.manage.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.UserEntity;

/**
 * 
 *
 * @author: cjl
 *
 * @date 2019-12-24 19:48:56
 */
public interface UserService extends IService<UserEntity> {

    void sendEmail(ApplyMaintainEntity applyMaintain, String s);

}

