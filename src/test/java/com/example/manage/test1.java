package com.example.manage;

import com.example.manage.api.entity.UserEntity;
import com.example.manage.api.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author: cjl
 * @date: 2019/12/24
 * @time: 20:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class test1 {

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("ddddd");
        userService.save(userEntity);
    }
}
