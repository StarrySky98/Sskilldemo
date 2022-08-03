package com.example.sskilldemo.controller;


import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.IOrderService;
import com.example.sskilldemo.vo.OrderDetailVo;
import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-24
 */
@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        log.info("订单号：{}",orderId);
        OrderDetailVo orderDetailVo =orderService.detail(orderId);
        log.info("订单详细信息：{}",orderDetailVo);
        return RespBean.success(orderDetailVo);


    }

}
