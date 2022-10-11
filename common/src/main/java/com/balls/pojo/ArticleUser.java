package com.balls.pojo;

import org.springframework.data.relational.core.mapping.Column;

/** 用来获取用户名与头像
 *  前端无该实体类
 */
public class ArticleUser {
    private String uid;
    private String username;
    @Column("head_sculpture")
    private String headSculpture;

    public ArticleUser() {
    }

    public ArticleUser(String uid, String username, String headSculpture) {
        this.uid = uid;
        this.username = username;
        this.headSculpture = headSculpture;
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
}
