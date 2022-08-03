package com.example.sskilldemo.utils;

import java.util.UUID;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/7/20 18:05
 **/

public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("_","");
    }
}
