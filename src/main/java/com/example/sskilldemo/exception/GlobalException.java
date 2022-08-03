package com.example.sskilldemo.exception;

import com.example.sskilldemo.vo.RespBean;
import com.example.sskilldemo.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class GlobalException extends RuntimeException{
    private RespBeanEnum respBeanEnum;
}
