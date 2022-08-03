package com.example.sskilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sskilldemo.mapper.GoodsMapper;
import com.example.sskilldemo.pojo.Goods;
import com.example.sskilldemo.service.IGoodsService;
import com.example.sskilldemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }
    /**
     * @author zhangkang
     * @description 根据商品id获取商品详情
     * @date 2022/7/25 17:46
     * @param goodsId
     * @return com.example.sskilldemo.vo.GoodsVo
     */

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
