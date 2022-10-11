package com.balls.controller;

import com.balls.enums.StatusCode;
import com.balls.util.RedisUtil;
import com.balls.util.ResultUtil;
import com.balls.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/noLogin")
    public Map noLogin(){
        //用户未登录
        return ResultUtil.getResult(StatusCode.UNAUTHORIZED);
    }

    //发送验证码
    @GetMapping("/sendSms/{phoneNumbers}")
    public Map sendVerificationCode(@PathVariable("phoneNumbers") String phoneNumbers){
        smsUtil.sendVerificationCode(phoneNumbers);
        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //验证验证码
    @GetMapping("/verify/{phoneNumbers}/{verificationCode}")
    public Map verification(@PathVariable("phoneNumbers")String phoneNumbers,
                            @PathVariable("verificationCode")String verificationCode){

        //如果reids中有该手机号对应的验证码，说明验证码还未过期
        if (redisUtil.hasKey(phoneNumbers)) {
            //若验证码相同，则验证成功
            if(verificationCode.equals(redisUtil.get(phoneNumbers))){
                return ResultUtil.getResult(StatusCode.SUCCESS);
            }else{
                return ResultUtil.getResult(StatusCode.FAILURE);
            }
        }

        return ResultUtil.getResult(StatusCode.CODE_EXPIRED);
    }

}
