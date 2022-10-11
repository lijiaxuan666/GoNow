package com.balls.service.impl;

import com.balls.mapper.ArticleMapper;
import com.balls.mapper.RelationMapper;
import com.balls.pojo.ArticleUser;
import com.balls.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public List<ArticleUser> getfollow(String uid) {
        List<String> ouids= relationMapper.getfollow(uid);
        List<ArticleUser> list=new ArrayList<>();
        for(String ouid:ouids){
            list.add(articleMapper.getInfo(ouid));
        }
        return list;
    }


    public boolean follow(String uid, String ouid) {
        return relationMapper.follow(uid,ouid);
    }


    public boolean cancelFollow(String uid, String ouid) {
        return relationMapper.cancelFollow(uid,ouid);
    }

    public int countRelation(String uid){
        return relationMapper.countRelation(uid);
    }
}
