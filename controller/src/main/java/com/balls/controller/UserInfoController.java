package com.balls.controller;

import com.balls.enums.StatusCode;
import com.balls.pojo.User;
import com.balls.service.UserInfoService;
import com.balls.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    //获取用户全部信息
    @GetMapping("/getInfo")
    public Map getUserInfo(HttpServletRequest request) {

        String uid = request.getHeader("uid");
        User user = userInfoService.getUserInfo(uid);
        log.info("用户：" + uid + "尝试获取信息");
        return ResultUtil.getResult(StatusCode.SUCCESS, user);

    }

    //获取用户全部信息
    @GetMapping("/getInfo/{ouid}")
    public Map getUserInfo(HttpServletRequest request,
                           @PathVariable("ouid") String ouid) {

        String uid = request.getHeader("uid");
        User user = userInfoService.getUserInfo(uid,ouid);
        log.info("用户" + uid + "获取了"+ouid+"的信息");

        return ResultUtil.getResult(StatusCode.SUCCESS, user);

    }

    //修改用户名、个性签名、性别、邮箱、生日
    @PostMapping("/updateOthers")
    public Map updateOthers(@RequestBody User user, HttpServletRequest request) {

        user.setUid(request.getHeader("uid"));
        boolean state = userInfoService.updateOthers(user);
        log.info("用户：" + user.getUid() + "修改了个人信息");
        return ResultUtil.getResult(state ? StatusCode.SUCCESS : StatusCode.SERVER_ERROR);

    }

    //修改标签
    @PostMapping("/updateLabel")
    public Map updateLabel(@RequestBody User user, HttpServletRequest request) {

        String uid = request.getHeader("uid");
        boolean state = userInfoService.updateLabel(uid, user.getLabel());
        log.info("用户：" + uid + "修改了个人标签");
        return ResultUtil.getResult(state ? StatusCode.SUCCESS : StatusCode.SERVER_ERROR);

    }

    //修改头像
    @PostMapping("/updateHead")
    public Map updateHeadSculpture(HttpServletRequest request,
                                   @RequestPart("headSculpture") MultipartFile headSculpture) {

        String uid = request.getHeader("uid");
        String url = userInfoService.updateHeadSculpture(uid, headSculpture);
        log.info("用户：" + uid + "修改了头像，新的url为：" + url);
        return ResultUtil.getResult(StatusCode.SUCCESS, url);

    }

    //修改背景图片
    @PostMapping("/updateBack")
    public Map updateBackground(HttpServletRequest request,
                                @RequestPart("background") MultipartFile background) {

        String uid = request.getHeader("uid");
        String url = userInfoService.updateBackground(uid, background);
        log.info("用户：" + uid + "修改了背景，新的url为：" + url);
        return ResultUtil.getResult(StatusCode.SUCCESS, url);

    }

}
