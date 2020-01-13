package com.example.manage.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.manage.api.entity.ApplyMaintainEntity;
import com.example.manage.api.entity.UserEntity;
import com.example.manage.api.service.ApplyMaintainService;
import com.example.manage.api.service.UserService;
import com.example.manage.config.ConfigEntity;
import com.example.manage.utils.CommUtils;
import com.example.manage.utils.PageUtils;
import com.example.manage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;


/**
 * 
 *
 * @author: cjl
 *
 * @date 2019-12-24 19:48:56
 */
@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private ApplyMaintainService applyMaintainService;

    @Autowired
    private ConfigEntity config;

    /**
     * 登录
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R login(UserEntity user, HttpSession session){
        UserEntity userEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>()
                                        .eq("username", user.getUsername())
                                        .eq("password", user.getPassword()));

        if(userEntity != null){
            // 登录成功 将用户信息保存在session中
            session.setAttribute("user", userEntity);
            return R.ok();
        }else {
            return R.error("用户名或密码错误");
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/registered")
    public R registered(UserEntity user,@RequestParam("vCode") String vCode ,HttpSession session){

        String vCode1 = (String)session.getAttribute("vCode" + user.getEmail());

        if (!vCode.equals(vCode1)){
            return R.error("验证码错误");
        }

        UserEntity userEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>()
                                        .eq("username", user.getUsername()));

        // 判读用户名是否存在
        if(userEntity == null){

//            userEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>()
//                    .eq("email", user.getEmail()));
//
//            // 判断邮箱是否存在
//            if(userEntity == null){
//                user.setCreateDate(new Date());
//                // 都不存在 执行注册
//                boolean insert = userService.save(user);
//                return insert ? R.ok() : R.error("注册失败");
//            }else{
//                return R.error("该邮箱已经存在");
//            }

            boolean insert = userService.save(user);
            return insert ? R.ok() : R.error("注册失败");
        }else {
            return R.error("该用户名已经存在");
        }

    }

    /**
     * 找回密码
     * @param user
     * @return
     */
    @PostMapping("/rePass")
    public R rePass(UserEntity user,@RequestParam("vCode") String vCode ,HttpSession session){

        String vCode1 = (String)session.getAttribute("vCode" + user.getEmail());

        if (!vCode.equals(vCode1)){
            return R.error("验证码错误");
        }

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
                                                    .eq("username", user.getUsername())
                                                    .eq("email", user.getEmail());

        UserEntity one = userService.getOne(queryWrapper);

        if (one != null){
            UserEntity newEntity = new UserEntity();
            newEntity.setId(one.getId());
            newEntity.setPassword(user.getPassword());

            // 修改密码
            boolean b = userService.updateById(newEntity);

            if (b){
                return R.ok();
            }else {
                return R.error("找回密码失败");
            }
        }else{
            return R.error("用户名或邮箱错误");
        }



    }


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PostMapping("/updateInfo")
    public R updateInfo(UserEntity user, HttpSession session){
        // 不进行更新的参数
        user.setUsername(null);
        user.setPassword(null);
        user.setCreateDate(null);
        user.setEmail(null);

        boolean b = userService.updateById(user);

        if (b){
            // 更新成功 将新数据保存在session中
            UserEntity userEntity = userService.getById(user.getId());
            session.setAttribute("user", userEntity);

            return R.ok();
        }else{
            return R.error("更新失败");
        }


    }


    /**
     * 用户订单列表
     * @param param
     * @param session
     * @return
     */
    @GetMapping("/queryOrder")
    public R queryUser(@RequestParam Map<String, Object> param, HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute("user");

        Page page = PageUtils.getPage(param);
        String orderNumber = (String) param.get("orderNumber");
        String status = (String)param.get("status");

        IPage iPage = applyMaintainService.getBaseMapper().selectPage(page, new QueryWrapper<ApplyMaintainEntity>()
                .like(!StringUtils.isEmpty(orderNumber), "order_number", orderNumber)
                .eq(!StringUtils.isEmpty(status), "status", status)
                .eq("user_id", user.getId())
                .orderByDesc("create_date"));

        R pageResult = PageUtils.getPageResult(iPage);

        return pageResult;
    }


    /**
     * 更改邮箱
     * @param email
     * @return
     */
    @PostMapping("/updateEmail")
    public R updateEmail(@RequestParam("email") String email,@RequestParam("vCode") String vCode ,HttpSession session){
        String vCode1 = (String)session.getAttribute("vCode" + email);
        if(vCode.equals(vCode1)){
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("email", email));

            if (userEntity == null){
                userEntity = (UserEntity)session.getAttribute("user");

                UserEntity user = new UserEntity();
                user.setEmail(email);
                user.setId(userEntity.getId());
                boolean b = userService.updateById(user);

                if (b){
                    userEntity.setEmail(email);
                    session.setAttribute("user", userEntity);
                    return R.ok();
                }else {
                    return R.error("邮箱更改失败");
                }
            }else {
                return R.error("该邮箱已经存在不能进行绑定");
            }

        }else{
            return R.error("验证码错误");
        }
    }

    @PostMapping("/updatePass")
    public R updatePass(@RequestParam("password") String password,@RequestParam("vCode") String vCode, HttpSession session){
        UserEntity userEntity = (UserEntity)session.getAttribute("user");

        String vCode1 = (String)session.getAttribute("vCode" + userEntity.getEmail());

        if(vCode.equals(vCode1)){

            UserEntity user = new UserEntity();
            user.setPassword(password);
            user.setId(userEntity.getId());

            boolean b = userService.updateById(user);

            if (b){
                userEntity.setPassword(password);
                session.setAttribute("user", userEntity);
                return R.ok();
            }else {
                return R.error("密码更改失败");
            }


        }else{
            return R.error("验证码错误");
        }
    }

    @PostMapping("/headImage/{id}")
    public R headImage(@RequestParam("file") MultipartFile file,@PathVariable("id") Long id, HttpSession session){
        String s = CommUtils.fileUpload(file, config.getImagePath());
        if (!StringUtils.isEmpty(s)){
            UserEntity userEntity = new UserEntity();
            userEntity.setId(id);
            userEntity.setHeadImage(s);
            userService.updateById(userEntity);

            UserEntity userEntity1 = userService.getById(id);

            session.setAttribute("user", userEntity1);

            return R.ok();
        }

        return R.error();

    }









    /**
     * 获取当前登录用户的信息
     * @return
     */
    @GetMapping("/getLoginUserInfo")
    public R getLoginUserInfo(HttpSession session){
        UserEntity user = (UserEntity)session.getAttribute("user");
        if(user != null){
            user.setPassword("");
            return R.ok().put("data", user);
        }else {
            return R.error("用户未登录");
        }

    }

}
