package com.example.sskilldemo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/7/24 1:08
 **/

public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();
    /**
     * @author zhangkang
     * @description //将对象转换成json字符串
     * @date 2022/7/24 1:10 
     * @param obj 
     * @return java.lang.String 
     */
    
    public static String object2JsonStr(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @author zhangkang
     * @description 将字符串转换为对象
     * @date 2022/7/24 1:14
     * @param jsonStr
     * @param clazz
     * @return T
     */

    public static <T> T jsonStr2Object(String jsonStr, Class<T> clazz){
        try {
            return objectMapper.readValue(jsonStr.getBytes("UTF-8"),clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list=objectMapper.readValue(jsonStr,javaType);
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
