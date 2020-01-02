package com.example.manage.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.EmployeesEntity;
import com.example.manage.api.service.ApplyMaintainService;
import com.example.manage.api.service.EmployeesService;
import com.example.manage.api.service.UserService;
import com.example.manage.config.ConfigEntity;
import com.example.manage.constans.StatusConstans;
import com.example.manage.service.MailService;
import com.example.manage.utils.CommUtils;
import com.example.manage.utils.PageUtils;
import com.example.manage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @email 2434017367@qq.com
 * @author: zhy
 * @date: 2019/12/28
 * @time: 14:51
 */

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private ApplyMaintainService applyMaintainService;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ConfigEntity config;



    @PostMapping("/login")
    public R login(@RequestParam("workNumber") String workNumber,
                   @RequestParam("password") String password,
                   HttpSession session){

        QueryWrapper<EmployeesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_number", workNumber).eq("password", password).eq("level", StatusConstans.ADMIN);

        EmployeesEntity one = employeesService.getOne(queryWrapper);
        if (one != null){
            // 保存用户信息到session中
            session.setAttribute("admin", one);
            return R.ok();
        }else {
            return R.error("账号或密码错误");
        }


    }


    /**
     * 查询订单
     * @param param
     * @return
     */
    @GetMapping("/queryOrder")
    public R queryOrder(@RequestParam Map<String, Object> param){
        Page page = PageUtils.getPage(param);
        String orderNumber = (String) param.get("orderNumber");
        String status = (String)param.get("status");

        IPage<ApplyMaintainEntity> iPage = applyMaintainService.getBaseMapper().selectPage(page, new QueryWrapper<ApplyMaintainEntity>()
                .like(!StringUtils.isEmpty(orderNumber), "order_number", orderNumber)
                .eq(!StringUtils.isEmpty(status), "status", status)
                .orderByDesc("create_date"));

        // 封装维修员信息
        for (ApplyMaintainEntity record : iPage.getRecords()) {
            Integer status1 = record.getStatus();
            // 判断订单是否为接单或完成订单或派单的状态
            if (StatusConstans.ACCEPT.equals(status1) || StatusConstans.COMPLETE.equals(status1) || StatusConstans.ALLOT.equals(status1)){
                EmployeesEntity employeesEntity = employeesService.getOne(new QueryWrapper<EmployeesEntity>().eq("id", record.getEmployeeId()));
                record.setEmployees(employeesEntity);
            }
        }

        R pageResult = PageUtils.getPageResult(iPage);

        return pageResult;

    }

    /**
     * 员工列表
     * @param param
     * @return
     */
    @GetMapping("/queryEmp")
    public R queryEmp(@RequestParam Map<String, Object> param){
        Page page = PageUtils.getPage(param);
        String name = (String) param.get("name");
        String workNumber = (String) param.get("workNumber");
        String status = (String) param.get("status");

        IPage iPage = employeesService.getBaseMapper().selectPage(page, new QueryWrapper<EmployeesEntity>()
                                                                .like(!StringUtils.isEmpty(name), "name", name)
                                                                .like(!StringUtils.isEmpty(workNumber), "workNumber", workNumber)
                                                                .eq(!StringUtils.isEmpty(status), "status", status)
                                                                .ne("level", StatusConstans.ADMIN)
                                                                .orderByDesc("create_date"));

        R pageResult = PageUtils.getPageResult(iPage);

        return pageResult;
    }


    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */
    @GetMapping("/queryEmp/{id}")
    public R queryEmpBy(@PathVariable("id") Long id){
        EmployeesEntity employeesEntity = employeesService.getById(id);
        return R.ok().put("data", employeesEntity);
    }



    /**
     * 选择员工列表
     * @param param
     * @return
     */
    @GetMapping("/selectEmpList/{orderId}")
    public R selectEmpList(@RequestParam Map<String, Object> param, @PathVariable("orderId") Long orderId){
        Page page = PageUtils.getPage(param);
        String name = (String) param.get("name");
        String workNumber = (String)param.get("workNumber");

        // 获取订单信息
        ApplyMaintainEntity applyMaintainEntity = applyMaintainService.getById(orderId);
        Long employeeId = applyMaintainEntity.getEmployeeId();

        IPage<EmployeesEntity> iPage = employeesService.getBaseMapper().selectPage(page, new QueryWrapper<EmployeesEntity>()
                                                                        .like(!StringUtils.isEmpty(name), "name", name)
                                                                        .like(!StringUtils.isEmpty(workNumber), "work_number", workNumber)
                                                                        .eq("status", StatusConstans.EMP_NORMAL)
                                                                        .ne(employeeId != null,"id", employeeId));

        R pageResult = PageUtils.getPageResult(iPage);

        return pageResult;
    }


    /**
     * 添加员工带头像
     * @param employees
     * @return
     */
    @PostMapping("/addEmpHeadImage")
    public R addEmpHeadImage(@RequestParam("file") MultipartFile file, EmployeesEntity employees){

        // 判断工号是否已经存在
        EmployeesEntity workNumber = employeesService.getOne(new QueryWrapper<EmployeesEntity>().eq("work_number", employees.getWorkNumber()));

        if (workNumber != null){
            return R.error("工号已经存在");
        }

        // 保存头像
        String uuid = CommUtils.fileUpload(file, config.getImagePath());

        employees.setLevel(StatusConstans.EMP);
        employees.setStatus(StatusConstans.EMP_NORMAL);
        employees.setCreateDate(new Date());
        employees.setHeadImage(uuid);

        employeesService.save(employees);

        return R.ok();
    }

    /**
     * 添加员工
     * @param employees
     * @return
     */
    @PostMapping("/addEmp")
    public R addEmp(EmployeesEntity employees){

        // 判断工号是否已经存在
        EmployeesEntity workNumber = employeesService.getOne(new QueryWrapper<EmployeesEntity>().eq("work_number", employees.getWorkNumber()));

        if (workNumber != null){
            return R.error("工号已经存在");
        }

        employees.setLevel(StatusConstans.EMP);
        employees.setStatus(StatusConstans.EMP_NORMAL);
        employees.setCreateDate(new Date());

        employeesService.save(employees);

        return R.ok();
    }


    /**
     * 编辑员工带头像
     * @param employees
     * @return
     */
    @PostMapping("/updateEmpHeadImage")
    public R updateEmpHeadImage(@RequestParam("file") MultipartFile file, EmployeesEntity employees){

        // 保存头像
        String uuid = CommUtils.fileUpload(file,  config.getImagePath());

        employees.setLevel(null);
        employees.setStatus(null);
        employees.setWorkNumber(null);
        employees.setCreateDate(null);
        employees.setHeadImage(uuid);

        employeesService.updateById(employees);

        return R.ok();
    }


    /**
     * 编辑员工
     * @param employees
     * @return
     */
    @PostMapping("/updateEmp")
    public R updateEmp(EmployeesEntity employees){

        employees.setLevel(null);
        employees.setStatus(null);
        employees.setWorkNumber(null);
        employees.setCreateDate(null);

        employeesService.updateById(employees);

        return R.ok();
    }

    /**
     * 员工操作
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/operating/{id}/{status}")
    public R operating(@PathVariable("id") Long id, @PathVariable("status") Integer status){

        // 判断该操作是否为设置正常，否则判断是否有订单正在进行
        if(!StatusConstans.EMP_NORMAL.equals(status)){
            List<ApplyMaintainEntity> list = applyMaintainService.getBaseMapper().selectList(new QueryWrapper<ApplyMaintainEntity>()
                                                                                            .eq("employee_id", id)
                                                                                            .eq("status", StatusConstans.ACCEPT));

            if(list != null && list.size() > 0){
                return R.error("该账号还有订单未完成不能进行操作");
            }
        }


        // 对账号进行操作
        EmployeesEntity entity = employeesService.getById(id);

        boolean b = false;

        String sendContent = "";

        // 判断状态类型
        if (StatusConstans.EMP_NORMAL.equals(status) || StatusConstans.EMP_FORBID.equals(status) || StatusConstans.EMP_REST.equals(status)){
            entity.setStatus(status);
            b = employeesService.updateById(entity);
        }else {
            b = employeesService.removeById(id);
        }

        if (b){
            if (StatusConstans.EMP_NORMAL.equals(status)){
                sendContent = "你的账号已被管理员设置为正常模式";
            }else if (StatusConstans.EMP_FORBID.equals(status)){
                sendContent = "你的账号已被管理员设置为禁止模式";
            }else if (StatusConstans.EMP_REST.equals(status)){
                sendContent = "你的账号已被管理员设置为调休模式";
            }else {
                sendContent = "你的账号已被管理员删除";
            }

            mailService.sendSimpleMail(entity.getEmail(), "状态通知", sendContent);

            return R.ok();
        }else{
            return R.error("操作失败");
        }

    }


    /**
     * 分配订单
     * @param orderId
     * @param empId
     * @return
     */
    @PostMapping("/assignOrder/{orderId}/{empId}")
    public R assignOrder(@PathVariable("orderId") Long orderId,@PathVariable("empId") Long empId){
        // 查看该订单是否被分配过
        ApplyMaintainEntity applyMaintainEntity = applyMaintainService.getById(orderId);

        // 是否已经分配过
        boolean isAssign = applyMaintainEntity.getEmployeeId() == null ? false : true;

        applyMaintainEntity.setEmployeeId(empId);
        // 更改状态为派单
        applyMaintainEntity.setStatus(StatusConstans.ALLOT);
        boolean b = applyMaintainService.updateById(applyMaintainEntity);

        if (b){
            EmployeesEntity employeesEntity = employeesService.getById(empId);
            String sendEmpContent = "";
            if (isAssign){
                // 分配过 发送邮件告知之前的维修员该订单已经分配给其他维修员
                sendEmpContent = "管理员已将订单号为：" + applyMaintainEntity.getOrderNumber() + " 分配给了其他维修员。";

            }else {
                // 未分配过
                sendEmpContent = "管理员已将订单号为：" + applyMaintainEntity.getOrderNumber() + " 分配于你。";

                // 通知用户
                userService.sendEmail(applyMaintainEntity, "订单当前已经派单，等待维修人员接单。");
            }

            mailService.sendSimpleMail(employeesEntity.getEmail(), "订单通知", sendEmpContent);

            return R.ok();
        }else{
            return R.error("派单失败");
        }


    }


}
