package com.balls.service;


public interface LoginService {

    //根据手机号获取密码
    Boolean checkPassword(String phoneNumbers,String password);

    //注册
    boolean register(String phoneNumbers,String password);

    //获取uid
    String getUid(String phoneNumbers);

    //获取手机号
    String getPhoneNumbers(String uid);

    //判断账号是否被注册
    boolean isExists(String phoneNumbers);

    //忘记密码修改密码
    boolean forgetPassword(String phoneNumbers,String password);
}
