package com.study.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 *
 * @author tsl
 * @date 2021/10/27 10:31
 **/
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> stringObjectRedisTemplate = new RedisTemplate<>();
        //key序列化
        stringObjectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        //value序列化
        stringObjectRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //hash key序列化
        stringObjectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash value序列化
        stringObjectRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //注入连接工厂
        stringObjectRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringObjectRedisTemplate;
    }
}
