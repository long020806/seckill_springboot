package com.study.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/2 11:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
