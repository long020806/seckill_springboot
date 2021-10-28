package com.study.seckill.vo;

import com.study.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  商品返回对象
 *
 * @author tsl
 * @date 2021/10/27 15:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
