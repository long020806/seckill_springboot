package com.study.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.seckill.exception.GlobalException;
import com.study.seckill.mapper.OrderMapper;
import com.study.seckill.pojo.*;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IOrderService;
import com.study.seckill.service.ISeckillGoodsService;
import com.study.seckill.service.ISeckillOrderService;
import com.study.seckill.utils.MD5Util;
import com.study.seckill.utils.UUIDUtil;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.OrderDetailVo;
import com.study.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    ISeckillGoodsService iSeckillGoodsServiceService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ISeckillOrderService iSeckillOrderService;
    @Autowired
    IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Transactional
    @Override
    public Order secKill(User user, Goods goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //获取秒杀商品
        SeckillGoods seckillGoods = iSeckillGoodsServiceService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        //减少秒杀商品库存
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        //mybatis-plus update(wrapper)需要sqlset才行
        //update(entity,wrapper)需要id
        // boolean seckillGoodsResult = iSeckillGoodsServiceService.update(new UpdateWrapper<SeckillGoods>()
        //         .eq("stock_count", seckillGoods.getStockCount())
        //         .eq("id", seckillGoods.getId())
        //         .gt("stock_count", 0));
        boolean goodsRsult = iSeckillGoodsServiceService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));
        if(seckillGoods.getStockCount()<1){
            //判断是否还有库存
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());//插入自动返回id
        seckillOrder.setGoodsId(goods.getId());
        iSeckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsByGoodsId = goodsService.findGoodsByGoodsId(orderId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsByGoodsId);
        return orderDetailVo;
    }

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,path,60, TimeUnit.SECONDS);
        return path;
    }

    /**
     * 校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public Boolean checkPath(User user, Long goodsId,String path) {
        if(user==null||goodsId<0|| StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);

        return path.equals(redisPath);
    }
}
