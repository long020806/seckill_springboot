package com.study.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.seckill.pojo.SeckillOrder;
import com.study.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
