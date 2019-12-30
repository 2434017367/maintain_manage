package com.example.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @email 2434017367@qq.com
 * @author: gyh
 * @date: 2019/12/14
 * @time: 15:56
 */

@Controller
public class indexController {

    @GetMapping("/")
    public String toIndex(){
        return "redirect:/index1.html";
    }

    @GetMapping("/toMain")
    public String toMain(){
        return "main";
    }


    @GetMapping("/toForm/{name}")
    public String toFormView(@PathVariable("name") String name){
        return "form/" + name;
    }


    @GetMapping("/to/{name}")
    public String toView(@PathVariable("name") String name){
        return  name;
    }


    @GetMapping("/toAdmin/{name}")
    public String toAdminView(@PathVariable("name") String name, @RequestParam Map<String,Object> param, HttpServletRequest request){
        Set<String> keySet = param.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            request.setAttribute(key, param.get(key));
        }
        return "admin/" + name;
    }


    @GetMapping("/toEmp/{name}")
    public String toEmpView(@PathVariable("name") String name){
        return "emp/" + name;
    }

    @GetMapping("/outLogin/{type}")
    public String outLogin(@PathVariable("type") String type ,HttpSession session){

        session.removeAttribute(type);

        return "redirect:/index1.html";

    }


}
