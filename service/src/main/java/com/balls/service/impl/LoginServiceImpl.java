package com.balls.service.impl;

import com.balls.mapper.LoginMapper;
import com.balls.service.LoginService;
import com.balls.util.SmsUtil;
import com.balls.util.SnowFlakeGenerateIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


@Service
@PropertySource(value = "classpath:config/default.properties",encoding = "UTF-8")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;  //注册功能持久层

    @Autowired
    private SmsUtil smsUtil;//

    @Value("${default.head_sculpture}")
    private String headSculpture;

    @Value("${default.background}")
    private String background;

    /**  用户注册手机号账号与密码
     *
     * @param phoneNumbers  用户注册使用的手机号
     * @param password 用户登录密码
     * @return  是否注册成功
     */
    public boolean register(String phoneNumbers,String password){
        //注册成功后为用户生成一个全局唯一uid
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker = new SnowFlakeGenerateIdWorker(0L,0L);
        String uid = snowFlakeGenerateIdWorker.generateNextId();

        String md5Password= DigestUtils.md5DigestAsHex(password.getBytes());

        //为用户生成一个初始用户名
        String username="用户"+phoneNumbers;
        loginMapper.register(uid,phoneNumbers,md5Password);//将手机号、密码、uid放入账户表
        loginMapper.register_info(uid,phoneNumbers,username, headSculpture,background);//将手机号、uid、用户名、默认头像背景放入信息表
        return true;
    }

    /**  根据手机号获取登录密码
     *
     * @param phoneNumbers  用户的手机号
     * @return   查到的用户密码，若没有则为null
     */
    public Boolean checkPassword(String phoneNumbers,String password) {
        String pass=loginMapper.getPassword(phoneNumbers);
        return DigestUtils.md5DigestAsHex(password.getBytes()).equals(pass);
    }

    /** 根据手机号获取用户uid
     *
     * @param phoneNumbers
     * @return 用户uid
     */
    public String getUid(String phoneNumbers){
        return loginMapper.getUid(phoneNumbers);
    }

    /** g根据uid获取手机号
     *
     * @param uid 用户uid
     * @return 手机号
     */
    public String getPhoneNumbers(String uid){
        return loginMapper.getPhoneNumbers(uid);
    }

    /** 判断该手机号是否被注册
     *
     * @param phoneNumbers 手机号
     * @return true：被注册   false：未被注册
     */
    public boolean isExists(String phoneNumbers){
        if(loginMapper.isExists(phoneNumbers)>0)
            return true;
        return false;
    }

    /** 忘记密码后修改密码
     *
     * @param phoneNumbers 手机号码
     * @param password  用户新密码
     * @return
     */
    public boolean forgetPassword(String phoneNumbers, String password){
        String md5Password= DigestUtils.md5DigestAsHex(password.getBytes());
        return loginMapper.forgetPassword(phoneNumbers,md5Password);
    }
}
