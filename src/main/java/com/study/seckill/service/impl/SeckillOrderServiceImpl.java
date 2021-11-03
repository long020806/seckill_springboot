package com.study.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.seckill.mapper.SeckillOrderMapper;
import com.study.seckill.pojo.SeckillOrder;
import com.study.seckill.pojo.User;
import com.study.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId:成功 -1：失败 0：排队中
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder!=null){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else{
            return 0L;
        }
    }
}
