package com.example.manage;

import com.example.manage.api.entity.UserEntity;
import com.example.manage.api.service.UserService;
import com.example.manage.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @email 2434017367@qq.com
 * @author: zhy
 * @date: 2019/12/24
 * @time: 20:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class emailTest {

    @Autowired
    private MailService mailService;

    @Test
    public void test(){
       mailService.sendSimpleMail("2434017367@qq.com", "验证码", "4444\n44");
    }



}
