package com.study.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.seckill.exception.GlobalException;
import com.study.seckill.mapper.OrderMapper;
import com.study.seckill.pojo.*;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IOrderService;
import com.study.seckill.service.ISeckillGoodsService;
import com.study.seckill.service.ISeckillOrderService;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.OrderDetailVo;
import com.study.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Override
    public Order secKill(User user, Goods goods) {
        //获取秒杀商品
        SeckillGoods seckillGoods = iSeckillGoodsServiceService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        //减少秒杀商品库存
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        iSeckillGoodsServiceService.updateById(seckillGoods);
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
}
