package com.example.sskilldemo.controller;


import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.MQSender;
import com.example.sskilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-18
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MQSender mqSender;
    /**
     * @author zhangkang
     * @description //用户测试
     * @date 2022/7/27 16:06
     * @param user
     * @return com.example.sskilldemo.vo.RespBean
     */

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }
    /**
     * @author zhangkang
     * @description //测试MQ发送消息
     * @date 2022/8/3 19:24
     */

    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("Hello");
    }


}
