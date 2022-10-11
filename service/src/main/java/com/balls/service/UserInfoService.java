package com.balls.service;

import com.balls.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    //查找用户所有信息
    User getUserInfo(String uid);

    //查找其他用户所有信息
    User getUserInfo(String uid,String ouid);

    //修改用户名、个性签名、性别、邮箱、生日
    boolean updateOthers(User user);

    //修改头像
    String updateHeadSculpture(String uid,MultipartFile headSculpture);

    //修改背景图片
    String updateBackground(String uid, MultipartFile background);

    //修改标签
    boolean updateLabel(String uid,String label);

}
