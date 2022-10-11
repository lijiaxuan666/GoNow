package com.balls.controller;


import com.balls.enums.StatusCode;
import com.balls.pojo.ArticleUser;
import com.balls.service.RelationService;
import com.balls.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    //获取关注列表关注
    @GetMapping("/getFollow")
    public Map getfollow(HttpServletRequest request){
        String uid=request.getHeader("uid");
        List<ArticleUser> list=relationService.getfollow(uid);
        return ResultUtil.getResult(StatusCode.SUCCESS,list);
    }

    //关注
    @PostMapping("/follow/{ouid}")
    public Map follow(HttpServletRequest request,
                      @PathVariable("ouid") String ouid){
        String uid=request.getHeader("uid");
        relationService.follow(uid,ouid);
        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //取消关注
    @PostMapping("/cancelFollow/{ouid}")
    public Map cancelFollow(HttpServletRequest request,
                      @PathVariable("ouid") String ouid){
        String uid=request.getHeader("uid");
        relationService.cancelFollow(uid,ouid);
        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //统计关注数量
    @GetMapping("/countFollow")
    public Map countFollow(HttpServletRequest request){
        String uid=request.getHeader("uid");
        int count=relationService.countRelation(uid);
        return ResultUtil.getResult(StatusCode.SUCCESS,count);
    }

}
