package com.balls.pojo;


import org.springframework.data.relational.core.mapping.Column;

/** 用户评论
 *  comment_id字段是数据库自动生成的自增主键
 *  uid字段是用来动态获取评论用户的用户名与头像，防止用户更新信息后无法及时修改
 *  gmt_create使用数据库自动生成插入的时间
 */
public class ArticleComment {
    @Column("comment_id")
    private Integer commentId;//评论id
    private String uid;//评论者id
    private String username;//评论者用户名
    @Column("head_sculpture")
    private String headSculpture;//评论者头像
    @Column("article_id")
    private String articleId;//被评论文章id
    private String comment;//评论内容
    @Column("gmt_create")
    private String gmtCreate;//评论时间

    public ArticleComment() {
    }

    public ArticleComment(Integer commentId, String uid, String username,
                          String headSculpture, String articleId, String comment, String gmtCreate) {
        this.commentId = commentId;
        this.uid = uid;
        this.username = username;
        this.headSculpture = headSculpture;
        this.articleId = articleId;
        this.comment = comment;
        this.gmtCreate = gmtCreate;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
