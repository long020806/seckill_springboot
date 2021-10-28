package com.study.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.seckill.pojo.Goods;
import com.study.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 查询商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsByGoodsId(Long goodsId);
}
