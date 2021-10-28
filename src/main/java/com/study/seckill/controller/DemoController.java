package com.study.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TODO
 * @date 2021/10/18 16:08
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {
    /**
     * 测试页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/hello")
    public String Hello(Model model){
        model.addAttribute("name","seckill");
        return "hello";
    }
}
