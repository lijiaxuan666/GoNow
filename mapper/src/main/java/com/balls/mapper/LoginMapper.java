package com.balls.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 登录注册相关功能
 */
@Mapper
public interface LoginMapper {
    //注册新用户
    @Insert("INSERT INTO user_account (uid,phone_numbers,password) VALUES (#{uid},#{phoneNumbers},#{password})")
    boolean register(String uid,String phoneNumbers,String password);

    @Insert("INSERT INTO user_info (uid,phone_numbers,username,head_sculpture,background) VALUES (#{uid},#{phoneNumbers},#{username},#{headSculpture},#{background})")
    boolean register_info(String uid,String phoneNumbers,String username,String headSculpture,String background);

    //根据电话号码查询出用户的uid
    @Select("SELECT uid FROM user_account WHERE phone_numbers = #{phoneNumbers}")
    String getUid(String phoneNumbers);

    //根据手机号查找密码
    @Select("SELECT password FROM user_account WHERE phone_numbers = #{phoneNumbers}")
    String getPassword(String phoneNumbers);

    //根据uid查找手机号
    @Select("SELECT phone_numbers FROM user_account WHERE uid = #{uid}")
    String getPhoneNumbers(String uid);

    //判断用户是否已注册
    @Select("SELECT count(uid) FROM user_account WHERE phone_numbers = #{phoneNumbers}")
    int isExists(String phoneNumbers);

    //忘记密码修改密码
    @Update("UPDATE user_account SET password = #{password} WHERE phone_numbers = #{phoneNumbers}")
    boolean forgetPassword(String phoneNumbers,String password);

}
