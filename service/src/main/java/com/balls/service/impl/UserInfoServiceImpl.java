package com.balls.service.impl;

import com.balls.mapper.RelationMapper;
import com.balls.mapper.UserInfoMapper;
import com.balls.pojo.User;
import com.balls.service.UserInfoService;
import com.balls.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private OssUtil ossUtil;

    public User getUserInfo(String uid) {
        User user = userInfoMapper.getUserInfo(uid);
        user.setIsFollow(2);
        return user;
    }

    public User getUserInfo(String uid, String ouid) {
        User user = userInfoMapper.getUserInfo(ouid);
        if (uid.equals(ouid)) {
            user.setIsFollow(2);
            return user;
        }
        int count = relationMapper.isFollow(uid, ouid);
        if (count > 0) {
            user.setIsFollow(1);
        } else {
            user.setIsFollow(0);
        }
        return user;
    }

    public boolean updateOthers(User user) {
        return userInfoMapper.updateOthers(user.getUid(), user.getUsername(), user.getSignature(), user.getSex(), user.getEmail(), user.getBirthday());
    }

    public String updateHeadSculpture(String uid, MultipartFile headSculpture) {
        //将图片上传至oss并获取其url存储到数据库中
        String url = ossUtil.uploadFile(headSculpture, "head_sculpture");
        userInfoMapper.updateHeadSculpture(uid, url);
        return url;
    }

    public String updateBackground(String uid, MultipartFile background) {
        //将图片上传至oss并获取其url存储到数据库中
        String url = ossUtil.uploadFile(background, "background");
        userInfoMapper.updateBackground(uid, url);
        return url;
    }

    public boolean updateLabel(String uid, String label) {
        return userInfoMapper.updateLabel(uid, label);
    }


}
