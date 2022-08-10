package com.example.sskilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sskilldemo.pojo.SeckillOrder;
import com.example.sskilldemo.pojo.User;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    /**
     * @author zhangkang
     * @description //TODO 
     * @date 2022/8/9 18:21
 * @param user
 * @param goodsId
 * @return java.lang.Long
     */
    
    Long getResult(User user, Long goodsId);
}
