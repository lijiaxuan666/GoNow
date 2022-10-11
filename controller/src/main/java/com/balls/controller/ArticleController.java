package com.balls.controller;

import com.balls.enums.StatusCode;
import com.balls.pojo.ArticleComment;
import com.balls.pojo.ArticleContent;
import com.balls.service.ArticleService;
import com.balls.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    //获取文章
    @GetMapping("/getArticle/{articleId}")
    public Map getArticle(HttpServletRequest request,
                          @PathVariable("articleId") String articleId) throws IOException {

        ArticleContent article=articleService.getArticle(articleId);
        log.info("用户"+request.getHeader("uid")+"查看了文章"+articleId);

        return ResultUtil.getResult(StatusCode.SUCCESS,article);
    }

    //获取文章集合
    @GetMapping("/getArticleList")
    public Map getArticleList(HttpServletRequest request){

        String uid=request.getHeader("uid");
        List<ArticleContent> list=articleService.getArticleList(uid);
        log.info("用户:"+uid+"查找了他的所有文章");

        return ResultUtil.getResult(StatusCode.SUCCESS,list);
    }

    //添加文章
    @PostMapping("/addArticle")
    public Map addArticle(HttpServletRequest request,
                          @RequestBody ArticleContent articleContent) throws IOException, ParseException {

        articleContent.setUid(request.getHeader("uid"));
        articleService.addArticle(articleContent);
        log.info("用户:"+articleContent.getUid()+"添加了文章"+articleContent.getContent());

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //添加评论
    @PostMapping("/addComment")
    public Map addComment(HttpServletRequest request,
                          @RequestBody ArticleComment articleComment){

        articleComment.setUid(request.getHeader("uid"));
        articleService.addComment(articleComment);
        log.info("用户:"+articleComment.getUid()+"对文章"+articleComment.getArticleId()+"评论："+articleComment.getComment());

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //更新文章
    @PostMapping("/updateArticle")
    public Map updateArticle(HttpServletRequest request,
                             @RequestBody ArticleContent articleContent) throws IOException, ParseException {

        String uid=request.getHeader("uid");
        boolean state=articleService.updateArticle(articleContent,uid);
        log.info("用户"+uid+"修改了文章");

        return state?ResultUtil.getResult(StatusCode.SUCCESS):ResultUtil.getResult(StatusCode.DATA_FORMAT_ERROR);
    }

    //删除文章
    @PostMapping("/deleteArticle/{articleId}")
    public Map deleteArticle(HttpServletRequest request,
                             @PathVariable("articleId") String articleId){

        articleService.deleteArticle(articleId);
        log.info("用户"+request.getHeader("uid")+"删除了文章"+articleId);

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //删除评论
    @PostMapping("/deleteComment/{commentId}")
    public Map deleteComment(HttpServletRequest request,
                             @PathVariable("commentId") String commentId){

        articleService.deleteComment(commentId);
        log.info("用户"+request.getHeader("uid")+"删除了评论"+commentId);

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //根据地址模糊查询
    @GetMapping("/search/{address}")
    public Map searchAddress(HttpServletRequest request,
                             @PathVariable("address") String address){

        List<ArticleContent> list= articleService.searchAddress(address);
        log.info("用户"+request.getHeader("uid")+"模糊查询了"+address);

        return ResultUtil.getResult(StatusCode.SUCCESS,list);
    }

    //精选推送
    @GetMapping("/putChoice")
    public Map putChoice(){

        List<ArticleContent> list=articleService.putChoice();

        return ResultUtil.getResult(StatusCode.SUCCESS,list);
    }

    //关注推送
    @GetMapping("/putFollow")
    public Map putFollow(HttpServletRequest request) throws IOException {

        String uid=request.getHeader("uid");
        List<ArticleContent> list=articleService.putFollow(uid);

        return ResultUtil.getResult(StatusCode.SUCCESS,list);
    }

}
