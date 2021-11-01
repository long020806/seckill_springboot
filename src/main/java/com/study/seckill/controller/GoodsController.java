package com.study.seckill.controller;

import com.study.seckill.pojo.User;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IUserService;
import com.study.seckill.vo.DetailVo;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/26 15:11
 **/
@Controller
@Slf4j
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    /**
     * 跳转商品列表页
     * @return
     */
    @RequestMapping(value = "toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){
        //Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        // if(StringUtils.isEmpty(ticket)){
        //     return "login";
        // }
        // User user = (User) session.getAttribute(ticket);
        // User user = iUserService.getUserByCookie(ticket, request, response);
        // if(user==null){
        //     return "login";
        // }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",iGoodsService.findGoodsVo());
        //如果为空手动渲染，存入Redis并缓存
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html =  thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("goodsList")
    @ResponseBody
    public RespBean goodsList(Model model, User user){
        // if(StringUtils.isEmpty(ticket)){
        //     return "login";
        // }
        // // User user = (User) session.getAttribute(ticket);
        // User user = iUserService.getUserByCookie(ticket, request, response);
        // if(user==null){
        //     return "login";
        // }
        RespBean success = RespBean.success();
        success.setObject(iGoodsService.findGoodsVo());
        return success;
    }
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user,@PathVariable("goodsId") Long goodsId,HttpServletRequest request,HttpServletResponse response){
        // ValueOperations valueOperations = redisTemplate.opsForValue();
        // String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        // if(!StringUtils.isEmpty(html)){
        //     return html;
        // }

        // if(StringUtils.isEmpty(ticket)){
        //     return "login";
        // }
        // // User user = (User) session.getAttribute(ticket);
        // User user = iUserService.getUserByCookie(ticket, request, response);
        // if(user==null){
        //     return "login";
        // }
        // model.addAttribute("user",user);
        GoodsVo goodsVo = iGoodsService.findGoodsByGoodsId(goodsId);
        // model.addAttribute("goods",goodsVo);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date date = new Date();
        //秒杀状态 0未开始 1已开始 2已结束
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if(date.before(startDate)){
            secKillStatus =0;
            remainSeconds = ((int) ((startDate.getTime() - date.getTime()) / 1000));
        }else if(date.after(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        }else{
            secKillStatus = 1;
            remainSeconds = 0;
        }
        // model.addAttribute("remainSeconds",remainSeconds);
        // model.addAttribute("secKillStatus",secKillStatus);


        // WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        // html =  thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);
        // if(StringUtils.isEmpty(html)){
        //     valueOperations.set("goodsDetail:" + goodsId,html,60, TimeUnit.SECONDS);
        // }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        RespBean result = RespBean.success(detailVo);
        return result;
    }

}
