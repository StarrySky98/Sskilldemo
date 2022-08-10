package com.example.sskilldemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.sskilldemo.pojo.Order;
import com.example.sskilldemo.pojo.SeckillGoods;
import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.rabbitmq.MQSender;
import com.example.sskilldemo.rabbitmq.SeckillMessage;
import com.example.sskilldemo.service.IGoodsService;
import com.example.sskilldemo.service.IOrderService;
import com.example.sskilldemo.service.ISeckillGoodsService;
import com.example.sskilldemo.service.ISeckillOrderService;
import com.example.sskilldemo.utils.JsonUtil;
import com.example.sskilldemo.vo.GoodsVo;
import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀商品表 前端控制器
 * 优化前
 *  QPS:382
 *  TP99:15839ms
 *
 *  页面缓存后
 *  Qps:403
 *  Tp99:13261ms
 *  使用redis,加索引方式，解决超卖。
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
@Controller
@RequestMapping("/seckillGoods")
@Slf4j
public class SeckillGoodsController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();
    @RequestMapping(value = "/doSeckill")
    @ResponseBody
    public RespBean doSeckill( User user,Long goodsId){
        if(user == null){
            return RespBean.success(RespBeanEnum.SESSION_ERROR);
        }
        log.info("秒杀按钮：{}",user);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        String seckillOrderJson = (String) valueOperations.get("order:" +
                user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            log.info("重复抢购");
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记,减少Redis访问
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if (stock < 0) {
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 请求入队，立即返回排队中
        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.sendsecKillMessage(JsonUtil.object2JsonStr(message));
        return RespBean.success(0);
    }
    @RequestMapping(value = "/doSeckill2")
    @ResponseBody
    public RespBean doSeckill2( User user,Long goodsId){
        if(user == null){
            return RespBean.success(RespBeanEnum.SESSION_ERROR);
        }
        log.info("秒杀按钮：{}",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount()<1){
            log.info("库存小于1");
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
//                eq("user_id", user.getId()).eq(
//                "goods_id",
//                goodsId));
        String seckillOrderJson = (String)
                redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(!StringUtils.isEmpty(seckillOrderJson)){
            log.info("重复抢购");
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.secSkill(user,goods) ;
        return RespBean.success(order);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(),
                    goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });

    }
    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return orderId:成功，-1：秒杀失败，0：排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }
}
