package com.example.manage.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manage.api.dao.UserDao;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.UserEntity;
import com.example.manage.api.service.UserService;
import com.example.manage.constans.StatusConstans;
import com.example.manage.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private MailService mailService;

    /**
     * 发送邮件
     * @param applyMaintain
     */
    @Override
    public void sendEmail(ApplyMaintainEntity applyMaintain, String s) {
        String sex = StatusConstans.MAN.equals(applyMaintain.getSex()) ? "先生" : "女士";
        String appellative = applyMaintain.getName() + sex;

        String content = "亲爱的：" + appellative +
                "您提交的订单\n地址：" +
                applyMaintain.getAddress() +
                "\n维修说明：" + applyMaintain.getContent() +
                "\n订单号:" + applyMaintain.getOrderNumber() +
                "\n" + s;

        mailService.sendSimpleMail(applyMaintain.getEmail(), "订单信息", content);
    }
}
