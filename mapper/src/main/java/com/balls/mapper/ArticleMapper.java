package com.balls.mapper;

import com.balls.pojo.ArticleComment;
import com.balls.pojo.ArticleContent;
import com.balls.pojo.ArticleUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    //批量向elasticsearch上传用户文章
    List<ArticleContent> bulkAddArticle();

    //查找用户所有文章
    List<ArticleContent> getArticleList(String uid);

    //查找用户所有文章
    List<String> getArticleIdList(String uid);

    //查找用户文章
    ArticleContent getArticle(String articleId);

    //查找文章评论
    List<ArticleComment> getComment(String articleId);

    //增加用户文章
    boolean addArticle(String articleId,String uid,String content,String address,String time,String gmtCreate);

    //增加评论
    boolean addComment(String uid,String articleId,String comment,String gmtCreate);

    //获取用户名与头像
    ArticleUser getInfo(String uid);

    //修改文章
    boolean updateArticle(String articleId,String content,String address,String time);

    //删除文章（真删，该属性无需恢复）
    boolean deleteArticle(String articleId);

    //删除评论（真删，该属性无需恢复）
    boolean deleteComment(Integer commentId);

    //获取文章创建时间
    String getCreateTime(String articleId);

    //更新文章状态
    void updateArticleState(String articleId);
}
