package com.study.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/2 15:11
 **/
@SpringBootTest
public class SeckillDemoApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisScript<Boolean> redisScript;
    @Test
    public void testLock01(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //占位如果key不存在才可以设置成功
        Boolean aBoolean = valueOperations.setIfAbsent("k1", "v1");
        //占位成功进行操作
        if(aBoolean) {
            valueOperations.set("name", "xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name: " + name);
            Integer.parseInt("xxxx");
            //    操作结束删除键
                redisTemplate.delete("name");
        }else{
            System.out.println("有线程使用，请稍后");
        }
    }
    @Test
    public void testLock02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //占位如果key不存在才可以设置成功
        Boolean aBoolean = valueOperations.setIfAbsent("k1", "v1", 5,TimeUnit.SECONDS);
        //占位成功进行操作
        if(aBoolean) {
            valueOperations.set("name", "xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name: " + name);
            Integer.parseInt("xxxx");
            //    操作结束删除键
            redisTemplate.delete("name");
        }else{
            System.out.println("有线程使用，请稍后");
        }
    }
    @Test
    public void testLock03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean aBoolean = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        //占位成功进行操作
        if(aBoolean) {
            valueOperations.set("name", "xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name: " + name);
            System.out.println(valueOperations.get("k1"));
            Object re = redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(re);
        }else{
            System.out.println("有线程使用，请稍后");
        }
    }
}
