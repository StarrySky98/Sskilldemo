package com.example.sskilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sskilldemo.exception.GlobalException;
import com.example.sskilldemo.mapper.UserMapper;
import com.example.sskilldemo.pojo.User;
import com.example.sskilldemo.service.IUserService;
import com.example.sskilldemo.utils.*;
import com.example.sskilldemo.vo.LoginVo;
import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangkang
 * @since 2022-07-18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo loginVo,HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user= userMapper.selectById(mobile);
        log.info("{}",user);
        System.out.println("传输的密码："+MD5Util.formPassToDbPass(password,user.getSalt()));
        System.out.println("查询的密码："+user.getPassword());
        if(user==null){
           throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.formPassToDbPass(password,user.getSalt()).equals(user.getPassword())){
           throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookies
        String ticket= UUIDUtil.uuid();
        //将用户存入redis
        redisTemplate.opsForValue().set("user:" + ticket,JsonUtil.object2JsonStr(user));
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        log.info("查到的密码："+MD5Util.formPassToDbPass(password,user.getSalt()));
        log.info("传输的密码："+user.getPassword());
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        String userjson = (String) redisTemplate.opsForValue().get("user:"+userTicket);
        User user= JsonUtil.jsonStr2Object(userjson,User.class);
        log.info("userSer{}",userjson);
        if(user != null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);

        }
        return user;
    }
    /**
     * @author zhangkang
     * @description //更新密码
     * @date 2022/7/29 14:04
     * @param userTicket
     * @param userId
     * @param password
     * @return com.example.sskilldemo.vo.RespBean
     */

    @Override
    public RespBean updatePassword(String userTicket, Long userId, String password) {
        User user=userMapper.selectById(userId);
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);

        }
        user.setPassword(MD5Util.inputPassToDbPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if(1==result){
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }

        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }


}
