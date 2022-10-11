package com.balls.controller;

import com.balls.enums.StatusCode;
import com.balls.service.LoginService;
import com.balls.util.RedisUtil;
import com.balls.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisUtil redisUtil;

    //注册
    @PostMapping("/register/{phoneNumbers}/{password}/{verificationCode}")
    public Map register(@PathVariable("phoneNumbers") String phoneNumbers,
                                   @PathVariable("password") String password,
                                   @PathVariable("verificationCode") String verificationCode){

        //如果账号已经被注册，则注册失败
        if(loginService.isExists(phoneNumbers)){
            return ResultUtil.getResult(StatusCode.FORBIDDEN);
        }

        //如果reids中有该手机号对应的验证码，说明验证码还未过期
        if (redisUtil.hasKey(phoneNumbers)) {
            //若验证码相同，则注册成功
            if(verificationCode.equals(redisUtil.get(phoneNumbers))){
                loginService.register(phoneNumbers,password);
                return ResultUtil.getResult(StatusCode.SUCCESS);
            }else{
                return ResultUtil.getResult(StatusCode.FAILURE);
            }
        }

        return ResultUtil.getResult(StatusCode.CODE_EXPIRED);

    }

    //手机号+密码登录+手机token(手机唯一标识)
    @PostMapping("/logon/{phoneNumbers}/{password}/{mobileToken}")
    public Map logOn(@PathVariable("phoneNumbers") String phoneNumbers,
                     @PathVariable("password") String password,
                     @PathVariable("mobileToken") String mobileToken){


        if(loginService.checkPassword(phoneNumbers,password)){

            if(redisUtil.hExists("Online",phoneNumbers)){//如果在线用户中这个账号已被登录
                String oldMoblie=(String)redisUtil.hGet("Online",phoneNumbers);//获取在线的设备
                if(!mobileToken.equals(oldMoblie))//如果该该设备不是上一次该账号登录的设备，说明是异地登陆
                    redisUtil.set(oldMoblie,"DOWN");//使上次登录的设备下线,下次登录时需要重新登录
            }

            redisUtil.hPut("Online",phoneNumbers,mobileToken);//将当前设备设置为在线设备
            String uid=loginService.getUid(phoneNumbers);//登陆时获取用户uid放入redis中方便查询
            redisUtil.set(mobileToken,uid);//key：设备唯一标识 value：用户全局唯一uid
            redisUtil.expire(mobileToken,15, TimeUnit.DAYS);//设置登录状态持续15天

            return ResultUtil.getResult(StatusCode.SUCCESS,uid);
        }

        return ResultUtil.getResult(StatusCode.FAILURE);

    }

    //检测登录状态
    @PostMapping("/state/{mobileToken}")
    public Map loginStatus(@PathVariable("mobileToken") String mobileToken){

        if(redisUtil.hasKey(mobileToken)){
            String uid=redisUtil.get(mobileToken);
            if(uid.equals("DOWN")){//若有异地登录下线标志
                return ResultUtil.getResult(StatusCode.REMOTE_LOGIN);
            }else{
                //若redis中有该设备的key，且无异地登录情况，则直接免登录。返回用户uid方便后续查询
                return ResultUtil.getResult(StatusCode.SUCCESS,uid);
            }
        }else
            return ResultUtil.getResult(StatusCode.NOT_FOUND);

    }

    //退出登录
    @PostMapping("/logoff/{mobileToken}")
    public Map logOff(@PathVariable("mobileToken")String mobileToken){

        //移除该设备存储的登录信息，表示该设备未登录任何账号
        if(redisUtil.hasKey(mobileToken)){
            String phoneNumbers=loginService.getPhoneNumbers(redisUtil.get(mobileToken));
            redisUtil.delete(mobileToken);
            redisUtil.hDelete("Online",phoneNumbers);//将其从在线用户中剔除，防止异地登陆误判
        }
        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //忘记密码
    @PostMapping("/forget/{phoneNumbers}/{password}")
    public Map forgetPassword(@PathVariable("phoneNumbers")String phoneNumbers,
                              @PathVariable("password")String password){

        if(loginService.forgetPassword(phoneNumbers,password))
            return ResultUtil.getResult(StatusCode.SUCCESS);
        else
            return ResultUtil.getResult(StatusCode.SERVER_ERROR);
    }
}
