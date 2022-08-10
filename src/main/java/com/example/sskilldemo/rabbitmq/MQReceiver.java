package com.example.sskilldemo.rabbitmq;

import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.IGoodsService;
import com.example.sskilldemo.service.IOrderService;
import com.example.sskilldemo.utils.JsonUtil;
import com.example.sskilldemo.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/8/3 19:16
 **/
@Slf4j
@Service
public class MQReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("QUEUE接受消息：" + msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg,
                SeckillMessage.class);
        Long goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购

                String seckillOrderJson = (String)
                        redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return;
        }
        orderService.secSkill(user, goods);
    }


    @RabbitListener(queues = "queue_topic01")
    public void receive01(Object msg) {
        log.info("QUEUE01接受消息：" + msg);
    }
    @RabbitListener(queues = "queue_topic02")
    public void receive02(Object msg) {
        log.info("QUEUE02接受消息：" + msg);
    }
}
