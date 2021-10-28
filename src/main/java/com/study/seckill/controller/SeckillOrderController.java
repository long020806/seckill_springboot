package com.study.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.seckill.pojo.Order;
import com.study.seckill.pojo.SeckillOrder;
import com.study.seckill.pojo.User;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IOrderService;
import com.study.seckill.service.ISeckillOrderService;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
@Controller
@RequestMapping("/seckill")
public class SeckillOrderController {
    @Autowired
    IGoodsService iGoodsService;
    @Autowired
    ISeckillOrderService iSeckillOrderService;
    @Autowired
    IOrderService iOrderService;
    /**
     * 秒杀功能
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("doSeckill")
    public String doSecKill(Model model, User user,Long goodsId){
        if(user == null) return "login";
        model.addAttribute("user",user);
        GoodsVo goods = iGoodsService.findGoodsByGoodsId(goodsId);
        if(goods.getStockCount()<1){
            model.addAttribute("errMsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = iSeckillOrderService.getOne(
                new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId())
                        .eq("goods_id", goodsId));
        if(seckillOrder !=null){
            model.addAttribute("errMsg",RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = iOrderService.secKill(user, goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }
}
