package com.example.manage.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.EmployeesEntity;
import com.example.manage.api.entity.UserEntity;
import com.example.manage.api.service.ApplyMaintainService;
import com.example.manage.api.service.EmployeesService;
import com.example.manage.api.service.UserService;
import com.example.manage.constans.StatusConstans;
import com.example.manage.service.MailService;
import com.example.manage.utils.CommUtils;
import com.example.manage.utils.R;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;


/**
 * 
 *
 * @author zhy
 * @email 2434017367@qq.com
 * @date 2019-12-24 19:48:56
 */
@RestController
@RequestMapping("api/apply")
public class ApplyMaintainController {
    @Autowired
    private ApplyMaintainService applyMaintainService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private EmployeesService employeesService;

    /**
     * 提交订单
     * @param applyMaintain
     * @return
     */
    @PostMapping("/submitApplyPrivate")
    public R submitApplyPrivate(@RequestParam("vCode") String vCode, ApplyMaintainEntity applyMaintain, HttpSession session){

        System.out.println("vCode = " + vCode);
        System.out.println("applyMaintain = " + new Gson().toJson(applyMaintain));

        // 获取验证码
        String vCode1 = (String)session.getAttribute("vCode" + applyMaintain.getEmail());

        if (vCode.equals(vCode1)){
            // 获取用户id
            UserEntity user = (UserEntity)session.getAttribute("user");

            if(user != null){
                applyMaintain.setUserId(user.getId());
            }

            applyMaintain.setCreateDate(new Date());
            applyMaintain.setStatus(StatusConstans.SUBMIT);
            // 获取订单号
            applyMaintain.setOrderNumber(CommUtils.getOrderNumber());

            // 保存订单信息
            applyMaintainService.save(applyMaintain);

            // 发送邮箱
            userService.sendEmail(applyMaintain, "提单成功，后续我们会以邮件的方式提醒您请注意查收。");

            return R.ok();
        }else {
            return R.error("验证码错误");
        }

    }


    /**
     * 取消订单
     * @param id
     * @return
     */
    @PostMapping("/cancel/{id}")
    public R cancel(@PathVariable("id") Long id){
        ApplyMaintainEntity applyMaintainEntity = applyMaintainService.getById(id);
        Integer status = applyMaintainEntity.getStatus();

        // 订单状态在提单或派单状态的情况下才能取消订单
        if(StatusConstans.SUBMIT.equals(status) || StatusConstans.ALLOT.equals(status)){
            applyMaintainEntity.setStatus(StatusConstans.CANCEL);
            boolean b = applyMaintainService.updateById(applyMaintainEntity);

            if (b){
                // 订单状态为派单状态时取消订单成功则要向已派单的维修员发送邮件提示
                if (StatusConstans.ALLOT.equals(status)){
                    String sendContent = "订单号为：" + applyMaintainEntity.getOrderNumber() + " 用户已取消订单";

                    // 获取维修员邮箱
                    EmployeesEntity employeesEntity = employeesService.getById(applyMaintainEntity.getEmployeeId());
                    String email = employeesEntity.getEmail();

                    mailService.sendSimpleMail(email, "订单通知", sendContent);
                }
                return R.ok();
            }else {
                return R.error("订单取消失败");
            }
        }else{
            return R.error("当前订单状态无法撤销订单");
        }
    }


    /**
     * 获取订单详情
     * @param orderNumber
     * @return
     */
    @GetMapping("/getInfo/{orderNumber}")
    public R getInfo(@PathVariable("orderNumber") String orderNumber){
        ApplyMaintainEntity applyMaintainEntity = applyMaintainService.getOne(new QueryWrapper<ApplyMaintainEntity>().eq("order_number", orderNumber));

        if (applyMaintainEntity == null){
            return R.error();
        }

        Integer status = applyMaintainEntity.getStatus();

        // 订单状态为接单或订单完成可以获取维修员信息
        if (StatusConstans.ACCEPT.equals(status) || StatusConstans.COMPLETE.equals(status)){
            EmployeesEntity employeesEntity = employeesService.getById(applyMaintainEntity.getEmployeeId());

            if(employeesEntity != null){
                employeesEntity.setPassword(null);
                employeesEntity.setId(null);
                employeesEntity.setLevel(null);

                applyMaintainEntity.setEmployees(employeesEntity);

            }

        }

        return R.ok().put("data", applyMaintainEntity);
    }




}
