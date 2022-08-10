package com.example.sskilldemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author zhangkang88
 * @Description
 * @date 2022/8/3 18:29
 **/
@Configuration
public class RabbitMQConfig {
    private static final String QUEUE = "seckillQueue";
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
    public Binding binding01(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
