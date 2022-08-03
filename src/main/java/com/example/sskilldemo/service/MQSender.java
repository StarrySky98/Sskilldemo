package com.example.sskilldemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/8/3 19:15
 **/
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void send(Object msg){
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend("queue",msg);
    }
}
