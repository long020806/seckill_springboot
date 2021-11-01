package com.study.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.seckill.pojo.Goods;
import com.study.seckill.pojo.Order;
import com.study.seckill.pojo.User;
import com.study.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
public interface IOrderService extends IService<Order> {
    Order secKill(User user, Goods goods);

    OrderDetailVo detail(Long orderId);
}
