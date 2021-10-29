package com.study.seckill.controller;


import com.study.seckill.pojo.User;
import com.study.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tsl
 * @since 2021-10-19
 */
@Controller
@RequestMapping("/user")
public class  UserController {
    /**
     * 用户信息
     * @param user
     * @return
     */
    @RequestMapping("info")
    public RespBean info(User user){
        return  RespBean.success(user);
    }
}
