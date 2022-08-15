package com.example.sskilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sskilldemo.exception.GlobalException;
import com.example.sskilldemo.mapper.OrderMapper;
import com.example.sskilldemo.mapper.SeckillOrderMapper;
import com.example.sskilldemo.pojo.Order;
import com.example.sskilldemo.pojo.SeckillGoods;
import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.IGoodsService;
import com.example.sskilldemo.service.IOrderService;
import com.example.sskilldemo.service.ISeckillGoodsService;
import com.example.sskilldemo.service.ISeckillOrderService;
import com.example.sskilldemo.utils.JsonUtil;
import com.example.sskilldemo.utils.MD5Util;
import com.example.sskilldemo.utils.UUIDUtil;
import com.example.sskilldemo.vo.GoodsVo;
import com.example.sskilldemo.vo.OrderDetailVo;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @author zhangkang
     * @description 秒杀
     * @date 2022/7/26 21:34
     * @param user
     * @param goodsVo
     * @return com.example.sskilldemo.pojo.Order
     */

    @Override
    @Transactional
    public Order secSkill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new
                QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        boolean seckillGoodsResult = seckillGoodsService.update(
                new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count- 1").eq("goods_id", goods.getId()).gt("stock_count", 0));
        // seckillGoodsService.updateById(seckillGoods);
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        valueOperations.set("order:" + user.getId() + ":" + goods.getId(),
                JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }

    @Transactional
    public Order secSkill2(User user, GoodsVo goodsVo) {
        //秒杀减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq
                ("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean seckillGoodResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count",
                seckillGoods.getStockCount()).eq("id",
                seckillGoods.getId()).gt("stock_count", 0));
        //seckillGoodsService.updateById(seckillGoods);
        if(!seckillGoodResult){
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" +
                        goodsVo.getId(),
                JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }
    /**
     * @author zhangkang
     * @description 订单详情
     * @date 2022/7/29 15:56
     * @param orderId
     * @return com.example.sskilldemo.vo.OrderDetailVo
     */

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        SeckillOrder seckillOrder = seckillOrderMapper.selectById(orderId);
        Order order = orderMapper.selectById(seckillOrder.getOrderId());
        log.info("已经查到订单：{}",seckillOrder);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(seckillOrder.getGoodsId());
        log.info("已经查到商品：{}",goodsVo);
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);

        log.info("订单信息：{}",detailVo);
        return detailVo;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user==null|| StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" +
                user.getId() + ":" + goodsId);
        return path.equals(redisPath);

    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");

        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" +
                goodsId, str, 60, TimeUnit.SECONDS);
        return str;

    }

}
