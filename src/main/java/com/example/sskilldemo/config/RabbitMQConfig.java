package com.example.sskilldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;


/**
 * @author zhangkang88
 * @Description
 * @date 2022/8/3 18:29
 **/
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue(){
        return new Queue("queue",true);
    }

}
