package com.example.sskilldemo.vo;

import com.example.sskilldemo.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/7/24 21:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {

    private BigDecimal seckillPrice;



    private Integer stockCount;


    private Date startDate;


    private Date endDate;
}
