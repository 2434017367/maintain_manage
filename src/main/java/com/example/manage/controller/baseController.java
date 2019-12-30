package com.example.manage.controller;

import com.example.manage.config.ConfigEntity;
import com.example.manage.service.MailService;
import com.example.manage.utils.CommUtils;
import com.example.manage.utils.FileUtlis;
import com.example.manage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;

/**
 * @email 2434017367@qq.com
 * @author: zhy
 * @date: 2019/12/16
 * @time: 11:09
 */

@Controller
public class baseController {

    @Autowired
    private ConfigEntity config;

    @Autowired
    private MailService mailService;

    /**
     * 图片获取
     * @param imageName
     * @param response
     * @throws Exception
     */
    @GetMapping("/getImage/{imageName}")
    public void getImage(@PathVariable(value = "imageName") String imageName, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");

        // 拼接图片路径
        String imageUrl = config.getImagePath() + imageName;
        File iamgeFile = new File(imageUrl);

        // 判断图片是否存在
        if (iamgeFile.exists()){
            FileInputStream fileInputStream = new FileInputStream(iamgeFile);
            ServletOutputStream outputStream = response.getOutputStream();

            FileUtlis.flow(fileInputStream, outputStream);
        }
    }

    @ResponseBody
    @GetMapping("/getVCode/{email}")
    public R getVCode(@PathVariable("email") String email, HttpSession session){

        // 生成验证码
        String vCode = CommUtils.getVCode();

        System.out.println("email:" + email + " vCode:" + vCode);

        // 发送验证码
        mailService.sendSimpleMail(email, "验证码", vCode);

        // 将验证码保存在session中
        session.setAttribute("vCode"+email, vCode);

        return R.ok();
    }


}
