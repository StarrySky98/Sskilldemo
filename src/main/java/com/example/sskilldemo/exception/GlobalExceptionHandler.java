package com.example.sskilldemo.exception;

import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*

 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @author zhangkang
     * @description //异常处理
     * @date 2022/7/20 17:35
     * @param e
     * @return com.example.sskilldemo.vo.RespBean
     */

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
           GlobalException ex = (GlobalException) e;
           return RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException){
            BindException ex= (BindException) e;
            RespBean respBean=RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常："+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;

        }
        return RespBean.error(RespBeanEnum.ERROR);
    }





}
