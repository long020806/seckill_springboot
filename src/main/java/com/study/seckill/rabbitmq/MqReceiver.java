package com.study.seckill.rabbitmq;

import com.study.seckill.pojo.SeckillMessage;
import com.study.seckill.pojo.SeckillOrder;
import com.study.seckill.pojo.User;
import com.study.seckill.service.IGoodsService;
import com.study.seckill.service.IOrderService;
import com.study.seckill.utils.JSONUtil;
import com.study.seckill.vo.GoodsVo;
import com.study.seckill.vo.RespBean;
import com.study.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Rabbitmq 消费者
 *
 * @author tsl
 * @date 2021/11/1 16:42
 **/
@Service
@Slf4j
public class MqReceiver {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    IGoodsService iGoodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IOrderService iOrderService;

    // @RabbitListener(queues = "queue")
    // public void receive(Object msg){
    //     log.info("接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_fanout01")
    // public void receive01(Object msg){
    //     log.info("QUEUE01接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_fanout02")
    // public void receive02(Object msg){
    //     log.info("QUEUE02接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_direct01")
    // public void receive03(Object msg){
    //     log.info("QUEUE01接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_direct02")
    // public void receive04(Object msg){
    //     log.info("QUEUE02接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_topic01")
    // public void receive05(Object msg){
    //     log.info("QUEUE01接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_topic02")
    // public void receive06(Object msg){
    //     log.info("QUEUE02接收到消息： "+msg);
    // }
    // @RabbitListener(queues = "queue_headers01")
    // public void receive07(Message msg){
    //     log.info("QUEUE01接收Message对象： "+msg);
    //     log.info("接收的消息"+new String(msg.getBody()));
    // }
    // @RabbitListener(queues = "queue_headers02")
    // public void receive08(Message msg){
    //     log.info("QUEUE02接收Message对象： "+msg);
    //     log.info("接收的消息"+new String(msg.getBody()));
    // }
    @RabbitListener(queues = "seckillQueue")
    public void receiveSeckillMessage(String message){
        log.info("接收到秒杀信息"+message);
        SeckillMessage seckillMessage = JSONUtil.string2Obj(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        //获取商品并判断库存
        GoodsVo goodsVo = iGoodsService.findGoodsByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return;
        }
        //判断是否重复秒杀      获取用户已秒杀商品 （没人限购一件）
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder !=null){
            return ;
        }
        //下单
        iOrderService.secKill(user,goodsVo);

    }



}
