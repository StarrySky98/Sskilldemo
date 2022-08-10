package com.example.sskilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sskilldemo.pojo.Order;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.vo.GoodsVo;
import com.example.sskilldemo.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
public interface IOrderService extends IService<Order> {

     Order secSkill(User user, GoodsVo goodsVo);

    OrderDetailVo detail(Long orderId);


}
