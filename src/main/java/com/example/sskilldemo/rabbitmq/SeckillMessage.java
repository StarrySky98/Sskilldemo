package com.example.sskilldemo.rabbitmq;

import com.example.sskilldemo.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/8/9 16:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
