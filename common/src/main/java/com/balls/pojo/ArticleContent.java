package com.balls.pojo;

import org.springframework.data.relational.core.mapping.Column;

import java.util.List;

/** 用户文章
 *  article_id字段由雪花算法生成全局唯一的Id
 *  uid字段是用来动态获取评论用户的用户名与头像，防止用户更新信息后无法及时修改
 *  comment字段是该文章的评论集合由业务层注入，减轻数据库压力
 */
public class ArticleContent {

    @Column("article_id")
    private String articleId;//文章id
    private String uid;//发布者id
    private String username;//发布者用户名
    @Column("head_sculpture")
    private String headSculpture;//发布者头像
    private String content;//文章内容
    private String address;//发布地点
    private String time;//约球时间
    @Column("gmt_create")
    private String gmtCreate;//发布时间
    private List<ArticleComment> comment;
    private Integer state;//是否结束


    public ArticleContent() {
    }

    public ArticleContent(String username, String headSculpture) {
        this.username = username;
        this.headSculpture = headSculpture;
    }

    public ArticleContent(String articleId, String uid, String username, String headSculpture,
                          String content, String address, String time, String gmtCreate,
                          List<ArticleComment> comment, Integer state) {
        this.articleId = articleId;
        this.uid = uid;
        this.username = username;
        this.headSculpture = headSculpture;
        this.content = content;
        this.address = address;
        this.time = time;
        this.gmtCreate = gmtCreate;
        this.comment = comment;
        this.state = state;
    }

    public String getArticleId() {
        return articleId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public List<ArticleComment> getComment() {
        return comment;
    }

    public void setComment(List<ArticleComment> comment) {
        this.comment = comment;
    }
}
