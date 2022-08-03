package com.example.sskilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sskilldemo.pojo.Goods;
import com.example.sskilldemo.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * @author zhangkang
     * @description //获取商品列表
     * @date 2022/7/24 21:57
     * @return java.util.List<com.example.sskilldemo.vo.GoodsVo>
     */

    List<GoodsVo> findGoodsVo();
    GoodsVo findGoodsVoByGoodsId(Long goodsId);

}
