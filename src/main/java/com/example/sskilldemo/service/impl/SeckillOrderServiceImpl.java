package com.example.sskilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sskilldemo.mapper.SeckillOrderMapper;
import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    /**
     * @author zhangkang
     * @description 获取秒杀结果
     * @date 2022/8/9 16:47
     * @param user
     * @param goodsId
     * @return java.lang.Long
     */
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new
                QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id",
                goodsId));
        if (null != seckillOrder) {
            return seckillOrder.getId();
        } else {
            if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
                return -1L;
            }else {
                return 0L;
            }
        }
    }
}
