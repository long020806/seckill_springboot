package com.study.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.seckill.pojo.User;
import com.study.seckill.vo.LoginVo;
import com.study.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsl
 * @since 2021-10-19
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    /**
     * 根据cookie获取用户
     * @param ticket
     * @return
     */
    User getUserByCookie(String ticket,HttpServletRequest request,HttpServletResponse response);
}
