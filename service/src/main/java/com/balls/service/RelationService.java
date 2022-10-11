package com.balls.service;

import com.balls.pojo.ArticleUser;

import java.util.List;

public interface RelationService {

    //查找用户关注的所有用户
    List<ArticleUser> getfollow(String uid);

    //关注
    boolean follow(String uid,String ouid);

    //取消关注
    boolean cancelFollow(String uid,String ouid);

    //统计该用户关注数量
    int  countRelation(String uid);

}
