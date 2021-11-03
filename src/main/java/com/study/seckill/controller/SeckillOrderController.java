package com.study.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.seckill.config.AccessLimit;
import com.study.seckill.pojo.Order;
import com.study.seckill.pojo.SeckillMessage;
import com.study.seckill.pojo.SeckillOrder;
import com.study.seckill.pojo.User;
import com.study.seckill.rabbitmq.MqSender;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IOrderService;
import com.study.seckill.service.ISeckillOrderService;
import com.study.seckill.utils.JSONUtil;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.RespBean;
import com.study.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
public class SeckillOrderController implements InitializingBean {
    @Autowired
    IGoodsService iGoodsService;
    @Autowired
    ISeckillOrderService iSeckillOrderService;
    @Autowired
    IOrderService iOrderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MqSender mqSender;
    @Autowired
    RedisScript<Long> redisScript;
    Map<Long,Boolean> emptyStockMap = new HashMap<>();
    /**
     * 秒杀功能
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "doSeckill2")
    public String doSecKill2(Model model, User user,Long goodsId){
        if(user == null) return "login";
        model.addAttribute("user",user);
        GoodsVo goods = iGoodsService.findGoodsByGoodsId(goodsId);
        Date nowDate = new Date();
        // if(nowDate.before(goods.getStartDate())){
        //     model.addAttribute("errMsg",RespBeanEnum.TIME_BEFORE);
        //     return "secKillFail";
        // }
        // if(nowDate.after(goods.getEndDate())){
        //     model.addAttribute("errMsg",RespBeanEnum.TIME_AFTER);
        //     return "secKillFail";
        // }
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
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId){
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        /*
        GoodsVo goods = iGoodsService.findGoodsByGoodsId(goodsId);
        Date nowDate = new Date();
        // if(nowDate.before(goods.getStartDate())){
        //     model.addAttribute("errMsg",RespBeanEnum.TIME_BEFORE);
        //     return "secKillFail";
        // }
        // if(nowDate.after(goods.getEndDate())){
        //     model.addAttribute("errMsg",RespBeanEnum.TIME_AFTER);
        //     return "secKillFail";
        // }
        if(goods.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
        // SeckillOrder seckillOrder = iSeckillOrderService.getOne(
        //         new QueryWrapper<SeckillOrder>()
        //                 .eq("user_id", user.getId())
        //                 .eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder !=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Order order = iOrderService.secKill(user, goods);
        return RespBean.success(order);
         */
        //rabbitmq 事件队列接入
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Boolean check = iOrderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder !=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        //内存标记，减少redis访问 返回库存不足
        if(emptyStockMap.get(goodsId)){
            return  RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //使用lua脚本增减库存，速度更快
        // Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        //redis预减库存 项目初始化时redis加载数据库库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);

        if(stock<0){
            //进入stock会变为-1  库存最低为0  增加1变成0 设置库存不足标识
            emptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //下单 发送消息的mq
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        //异步发送消息 返回正在排队中
        mqSender.sendSeckillMessage(JSONUtil.obj2String(seckillMessage));
        return RespBean.success(0);
    }

    /**
     * 获取真正秒杀接口 ， 隐藏真正接口防止脚本
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @RequestMapping(value = "path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean path(User user, Long goodsId, HttpServletRequest request){

        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //限制访问次数，5秒5此
        // String uri = request.getRequestURI();
        // Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
        // if(count ==null){
        //     valueOperations.set(uri + ":" + user.getId(),1,5, TimeUnit.SECONDS);
        // }else if(count<5){
        //     valueOperations.increment(uri + ":" + user.getId());
        // }else{
        //     return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REHEAD);
        // }

        String str = iOrderService.createPath(user,goodsId);
        return RespBean.success(str);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化redis 商品数量
        List<GoodsVo> list = iGoodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            //避免多次查询redis 二设置的库存为空标识
            emptyStockMap.put(goodsVo.getId(),false);
        });
    }

    /**
     * 秒杀结果查询：
     * @param user
     * @param goodsId
     * @return orderId:成功 -1：失败 0：排队中
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = iSeckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }
}
