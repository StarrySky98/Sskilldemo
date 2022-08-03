package com.example.sskilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sskilldemo.pojo.Goods;
import com.example.sskilldemo.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
