package com.study.seckill.controller;


import com.study.seckill.pojo.User;
import com.study.seckill.rabbitmq.MqSender;
import com.study.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    MqSender mqSender;
    /**
     * 用户信息
     * @param user
     * @return
     */
    @RequestMapping("info")
    public RespBean info(User user){
        return  RespBean.success(user);
    }

    /**
     * rabbitmq 测试
     */
    // @RequestMapping("mq")
    // @ResponseBody
    // public void mq(){
    //     // mqSender.send("hello");
    //     // mqSender.send01("hello");
    //     // mqSender.send02("hello");
    //     mqSender.send03("hello");
    // }
    // @RequestMapping("/mq/topic01")
    // @ResponseBody
    // public void mq01(){
    //
    //     mqSender.send03("hello");
    // }
    // @RequestMapping("/mq/topic02")
    // @ResponseBody
    // public void mq02(){
    //
    //     mqSender.send04("hello");
    // }
    // @RequestMapping("/mq/headers01")
    // @ResponseBody
    // public void mq03(){
    //
    //     mqSender.send05("hello");
    // }
    // @RequestMapping("/mq/headers02")
    // @ResponseBody
    // public void mq04(){
    //
    //     mqSender.send06("hello");
    // }
}
