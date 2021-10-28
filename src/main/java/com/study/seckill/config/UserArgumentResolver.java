package com.study.seckill.config;

import com.study.seckill.pojo.User;
import com.study.seckill.service.IUserService;
import com.study.seckill.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义用户参数
 *
 * @author tsl
 * @date 2021/10/27 11:06
 **/
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    IUserService iUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz== User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String userTicket = CookieUtil.getCookieValue(nativeRequest, "userTicket");
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = iUserService.getUserByCookie(userTicket, nativeRequest, nativeResponse);
        return user;
    }
}
