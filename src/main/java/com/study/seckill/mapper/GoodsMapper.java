package com.study.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.seckill.pojo.Goods;
import com.study.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tsl
 * @since 2021-10-27
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoById(Long goodsId);
}
