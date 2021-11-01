package com.study.seckill.vo;

import com.study.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/1 11:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    GoodsVo goodsVo;
    Order order;
}
