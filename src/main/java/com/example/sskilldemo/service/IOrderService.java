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

    /**
     * @author zhangkang
     * @description //校验路径
     * @date 2022/8/15 17:57
     * @param user
     * @param goodsId
     * @param path
     * @return boolean
     */

    boolean checkPath(User user, Long goodsId, String path);
    /**
     * @author zhangkang
     * @description 获取路径
     * @date 2022/8/15 17:58
     * @param user
     * @param goodsId
     * @return java.lang.String
     */

    String createPath(User user, Long goodsId);
}
