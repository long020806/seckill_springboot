package com.study.seckill.controller;

import com.study.seckill.service.IUserService;
import com.study.seckill.utils.ValidateUtil;
import com.study.seckill.vo.LoginVo;
import com.study.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/19 14:46
 **/
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    IUserService iUserService;
    /**
     * 跳转登录页面
     * @return
     */
    @RequestMapping("/toLogin")
    public  String toLogin(){
        return "login";
    }
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        log.info("{}",loginVo);
        return  iUserService.doLogin(loginVo,httpServletRequest,httpServletResponse);
    }
}
