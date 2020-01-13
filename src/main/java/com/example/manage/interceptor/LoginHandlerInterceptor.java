package com.example.manage.interceptor;

import com.example.manage.api.entity.EmployeesEntity;
import com.example.manage.api.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * 登录拦截器
 *
 * @author: cjl
 * @date: 2019/12/17
 * @time: 10:15
 */

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        String type = "";
        if (requestURI.contains("/toMain") || requestURI.contains("/api/user")) {
            type = "user";
        }else if(requestURI.contains("/toAdmin") || requestURI.contains("/api/admin")){
            type = "admin";
        }else if(requestURI.contains("/toEmp") || requestURI.contains("/api/emp")){
            type = "emp";
        }

        HttpSession session = request.getSession();
        Object obj  = session.getAttribute(type);

        if (obj != null){
            System.out.println(":进入拦截器 拦截目标：" + requestURI);
            return true;

        }

        response.sendRedirect(request.getContextPath() + "/index1.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
