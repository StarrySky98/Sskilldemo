package com.example.sskilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.vo.LoginVo;
import com.example.sskilldemo.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-18
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
    /**
     * @author zhangkang
     * @description //更新密码
     * @date 2022/7/29 14:04
     * @param userTicket
     * @param userId
     * @param password
     * @return com.example.sskilldemo.vo.RespBean
     */

    RespBean updatePassword(String userTicket,Long userId,String password);
}
