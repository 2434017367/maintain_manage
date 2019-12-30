package com.example.manage.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.EmployeesEntity;
import com.example.manage.api.service.ApplyMaintainService;
import com.example.manage.api.service.EmployeesService;
import com.example.manage.api.service.UserService;
import com.example.manage.constans.StatusConstans;
import com.example.manage.utils.PageUtils;
import com.example.manage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 
 *
 * @author zhy
 * @email 2434017367@qq.com
 * @date 2019-12-27 10:12:51
 */
@RestController
@RequestMapping("api/emp")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private ApplyMaintainService applyMaintainService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestParam("workNumber") String workNumber,
                   @RequestParam("password") String password,
                   HttpSession session){

        QueryWrapper<EmployeesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_number", workNumber).eq("password", password).eq("level", StatusConstans.EMP);

        EmployeesEntity one = employeesService.getOne(queryWrapper);
        if (one != null){
            // 保存用户信息到session中
            session.setAttribute("emp", one);
            return R.ok();
        }else {
            return R.error("账号或密码错误");
        }


    }


    /**
     * 获取订单列表
     * @param param
     * @param session
     * @return
     */
    @GetMapping("/queryOrder")
    public R queryOrder(@RequestParam Map<String, Object> param, HttpSession session){

        EmployeesEntity employees = (EmployeesEntity)session.getAttribute("emp");

        Page page = PageUtils.getPage(param);
        String orderNumber = (String) param.get("orderNumber");
        String status = (String)param.get("status");

        IPage iPage = applyMaintainService.getBaseMapper().selectPage(page, new QueryWrapper<ApplyMaintainEntity>()
                                                            .eq("employee_id", employees.getId())
                                                            .eq(!StringUtils.isEmpty(orderNumber),"order_number", orderNumber)
                                                            .eq(!StringUtils.isEmpty(status), "status", status));

        R pageResult = PageUtils.getPageResult(iPage);

        return pageResult;

    }

    /**
     * 获取登录员工信息
     * @param session
     * @return
     */
    @GetMapping("/getLoginEmpInfo")
    public R getLoginEmpInfo(HttpSession session){
        EmployeesEntity employees = (EmployeesEntity)session.getAttribute("emp");
        employees.setPassword(null);

        return R.ok().put("data", employees);
    }


    /**
     * 接单
     * @param orderId
     * @return
     */
    @PostMapping("/accept/{orderId}")
    public R accept(@PathVariable("orderId") String orderId, HttpSession session){
        EmployeesEntity employees = (EmployeesEntity)session.getAttribute("emp");

        // 根据订单号获取订单
        ApplyMaintainEntity applyMaintain = applyMaintainService.getById(orderId);

        // 判断订单状态是否为派单状态
        if (StatusConstans.ALLOT.equals(applyMaintain.getStatus())){
            applyMaintain.setEmployeeId(employees.getId());
            // 接单
            applyMaintain.setStatus(StatusConstans.ACCEPT);

            boolean b = applyMaintainService.updateById(applyMaintain);

            if (b){
                // 更新成功给用户发送邮件
                userService.sendEmail(applyMaintain, "你的订单已经被工号为：" + employees.getWorkNumber() + " 接单");
                return R.ok();
            }
        }

        return R.error("接单失败");
    }


    /**
     * 完成订单
     * @param orderId
     * @return
     */
    @PostMapping("/complete/{orderId}")
    public R complete(@PathVariable("orderId") String orderId, HttpSession session){
        EmployeesEntity employees = (EmployeesEntity)session.getAttribute("emp");

        // 根据订单号获取订单
        ApplyMaintainEntity applyMaintain = applyMaintainService.getById(orderId);

        // 判断订单状态是否为接单状态
        if (StatusConstans.ACCEPT.equals(applyMaintain.getStatus())){
            applyMaintain.setEmployeeId(employees.getId());
            // 完成订单
            applyMaintain.setStatus(StatusConstans.COMPLETE);

            boolean b = applyMaintainService.updateById(applyMaintain);

            if (b){
                // 更新成功给用户发送邮件
                userService.sendEmail(applyMaintain, "你的订单已经完成");
                return R.ok();
            }
        }

        return R.error("接单失败");
    }




}
