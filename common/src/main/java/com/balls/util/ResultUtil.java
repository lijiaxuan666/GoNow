package com.balls.util;

import com.balls.enums.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class ResultUtil {

    /** 封装返回的状态
     *
     * @param status 响应的状态
     * @return map集合
     */
    public static Map getResult(StatusCode status) {
        Map<String,Object> map=new HashMap<>();
        map.put("code",status.getCode());
        map.put("msg",status.getMsg());
        map.put("data",null);
        return map;
    }

    /** 封装返回的状态及数据
     *
     * @param status 响应的状态
     * @param data 响应的数据
     * @return map集合
     */
    public static Map getResult(StatusCode status,Object data) {
        Map<String,Object> map=new HashMap<>();
        map.put("code",status.getCode());
        map.put("msg",status.getMsg());
        map.put("data",data);
        return map;
    }
}
