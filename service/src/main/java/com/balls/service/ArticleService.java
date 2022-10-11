package com.balls.service;

import com.balls.pojo.ArticleComment;
import com.balls.pojo.ArticleContent;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    //根据uid获取文章信息
    ArticleContent getArticle(String articleId) throws IOException;

    //获取用户所有文章
    List<ArticleContent> getArticleList(String uid);

    //增加用户文章
    boolean addArticle(ArticleContent articleContent);

    //增加评论
    boolean addComment(ArticleComment articleComment);

    //修改文章
    boolean updateArticle(ArticleContent articleContent,String uid);

    //删除文章（假删，使文章is_deleteb变为真）
    boolean deleteArticle(String articleId);

    //删除评论（真删，该属性无需恢复）
    boolean deleteComment(String commentId);

    //模糊查询
    List<ArticleContent> searchAddress(String address);

    //精选推送，推送近7天内的球局
    List<ArticleContent> putChoice();

    List<ArticleContent> putFollow(String uid) throws IOException;
}
