package com.balls.mapper;


import com.balls.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户个人信息相关功能
 */
@Mapper
public interface UserInfoMapper {

    //查找用户所有信息
    @Select("SELECT uid,phone_numbers,username,signature,label,sex,email,birthday,head_sculpture,background FROM user_info WHERE uid = #{uid}")
    User getUserInfo(String uid);

    //修改头像
    @Update("UPDATE user_info SET head_sculpture = #{headSculpture} WHERE uid = #{uid}")
    boolean updateHeadSculpture(String uid,String headSculpture);

    //修改背景图片
    @Update("UPDATE user_info SET background = #{background} WHERE uid = #{uid}")
    boolean updateBackground(String uid,String background);

    //修改标签
    @Update("UPDATE user_info SET label = #{label} WHERE uid = #{uid}")
    boolean updateLabel(String uid,String label);

    //修改性别、邮箱、生日
    @Update("UPDATE user_info SET username = #{username},signature = #{signature},sex = #{sex},email = #{email},birthday = #{birthday} WHERE uid = #{uid}")
    boolean updateOthers(String uid, String username,String signature,Integer sex, String email, String birthday);
}
