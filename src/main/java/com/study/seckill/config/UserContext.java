package com.study.seckill.config;

import com.study.seckill.pojo.User;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/3 9:39
 **/
@Component
public class UserContext {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    public static void setUser(User user){
        userThreadLocal.set(user);
    }
    public static User getUser(){
        return userThreadLocal.get();
    }
}
