package com.example.sskilldemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sskilldemo.pojo.Order;
import com.example.sskilldemo.pojo.SeckillGoods;
import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.IGoodsService;
import com.example.sskilldemo.service.IOrderService;
import com.example.sskilldemo.service.ISeckillGoodsService;
import com.example.sskilldemo.service.ISeckillOrderService;
import com.example.sskilldemo.vo.GoodsVo;
import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

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
public class SeckillGoodsController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(value = "/doSeckill")
    @ResponseBody
    public RespBean doSeckill( User user,Long goodsId){
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


}
