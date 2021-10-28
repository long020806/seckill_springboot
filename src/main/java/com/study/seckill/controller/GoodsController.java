package com.study.seckill.controller;

import com.study.seckill.pojo.User;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IUserService;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/10/26 15:11
 **/
@Controller
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IGoodsService iGoodsService;


    /**
     * 跳转商品列表页
     * @return
     */
    @RequestMapping("toList")
    public String toList(Model model,User user){
        // if(StringUtils.isEmpty(ticket)){
        //     return "login";
        // }
        // // User user = (User) session.getAttribute(ticket);
        // User user = iUserService.getUserByCookie(ticket, request, response);
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",iGoodsService.findGoodsVo());
        return "goodsList";
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
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user,@PathVariable("goodsId") Long goodsId){
        // if(StringUtils.isEmpty(ticket)){
        //     return "login";
        // }
        // // User user = (User) session.getAttribute(ticket);
        // User user = iUserService.getUserByCookie(ticket, request, response);
        // if(user==null){
        //     return "login";
        // }
        model.addAttribute("user",user);
        GoodsVo goodsVo = iGoodsService.findGoodsByGoodsId(goodsId);
        model.addAttribute("goods",goodsVo);
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
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        return "goodsDetail";
    }

}
