package com.study.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/2 10:28
 **/
@Configuration
public class RabbitMQConfigHeader {
    // private static final String QUEUE01="queue_headers01";
    // private static final String QUEUE02="queue_headers02";
    // private static final String EXCHANGE="headersExchange";
    // private static final String HEADERS01="headers01";
    // private static final String HEADERS02="headers02";
    // @Bean
    // public Queue queue01(){
    //     return new Queue(QUEUE01);
    // }
    // @Bean
    // public Queue queue02(){
    //     return new Queue(QUEUE02);
    // }
    // @Bean
    // public HeadersExchange headersExchange(){
    //     return new HeadersExchange(EXCHANGE);
    // }
    // @Bean
    // public Binding binding01(){
    //     Map<String,Object> map=new HashMap<>();
    //     map.put("color","red");
    //     map.put("speed","low");
    //     return BindingBuilder.bind(queue01()).to(headersExchange()).whereAny(map).match();
    // }
    // @Bean
    // public Binding binding02(){
    //     Map<String,Object> map=new HashMap<>();
    //     map.put("color","red");
    //     map.put("speed","fast");
    //     return BindingBuilder.bind(queue02()).to(headersExchange()).whereAll(map).match();
    // }
}
