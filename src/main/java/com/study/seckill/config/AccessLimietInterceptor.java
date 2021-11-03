package com.study.seckill.config;

import com.study.seckill.pojo.User;
import com.study.seckill.service.IUserService;
import com.study.seckill.utils.CookieUtil;
import com.study.seckill.vo.RespBean;
import com.study.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/3 9:30
 **/
@Component
public class AccessLimietInterceptor implements HandlerInterceptor {
    @Autowired
    IUserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            User user = getUser(request,response);
            UserContext.setUser(user);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit methodAnnotation = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(methodAnnotation==null){
                return true;
            }
            int second = methodAnnotation.second();
            int maxCount = methodAnnotation.maxCount();
            boolean needLogin = methodAnnotation.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(user==null){
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key += ":"+user.getId();
            }
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
            if(count==null){
                valueOperations.set(key,1,5, TimeUnit.SECONDS);
            }else if(count < maxCount){
                valueOperations.increment(key);
            }else{
                render(response,RespBeanEnum.ACCESS_LIMIT_REHEAD);
                return false;
            }

        }
        return true;
    }

    private void render(HttpServletResponse response, RespBeanEnum sessionError) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        RespBean error = RespBean.error(sessionError);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }

    /**\
     * 获取当前用户
     * @param request
     * @param response
     * @return
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request,"userTicket");
        if(StringUtils.isEmpty(ticket)){
            return null;
        }
        return userService.getUserByCookie(ticket,request,response);
    }

}
