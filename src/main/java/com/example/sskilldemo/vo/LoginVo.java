package com.example.sskilldemo.vo;

import com.example.sskilldemo.validator.IsMobile;
import lombok.Data;
import org.attoparser.dom.INestableNode;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @IsMobile
    @NotNull(message = "手机号码不能为空")
    private String mobile;
    @NotNull
    private String password;
}
