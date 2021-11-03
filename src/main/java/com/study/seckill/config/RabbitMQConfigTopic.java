package com.study.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author tsl
 * @date 2021/11/2 10:16
 **/
@Configuration
public class RabbitMQConfigTopic {
    // private static final String QUEUE01="queue_topic01";
    // private static final String QUEUE02="queue_topic02";
    // private static final String EXCHANGE="topicExchange";
    // private static final String ROUTEKEY01="#.queue.#";
    // private static final String ROUTEKEY02="*.queue.#";

    // @Bean
    // public Queue queue01(){
    //     return new Queue(QUEUE01);
    // }
    // @Bean
    // public Queue queue02(){
    //     return new Queue(QUEUE02);
    // }
    // @Bean
    // public TopicExchange directExchange(){
    //     return new TopicExchange(EXCHANGE);
    // }
    // @Bean
    // public Binding binding01(){
    //     return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTEKEY01);
    // }
    // @Bean
    // public Binding binding02(){
    //     return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTEKEY02);
    // }
    private static final String QUEUE ="seckillQueue";
    private static final String EXCHANGE = "seckillExchange";
    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}
