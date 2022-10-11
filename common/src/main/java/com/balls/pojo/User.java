package com.balls.pojo;


import org.springframework.data.relational.core.mapping.Column;

/** 个人基础信息  */
public class User {

    private String uid;//用户id
    @Column("phone_numbers")
    private String phoneNumbers;//手机号码
    private String username;//用户昵称
    private String signature;//个性签名
    private String label;//标签/爱好
    private Integer sex;//性别 0女 1男
    private String email;//邮箱
    private String birthday;//生日
    @Column("head_sculpture")
    private String headSculpture;//头像
    private String background;//背景
    private int isFollow;//判断是否关注.未关注为0，已关注为1，用户本人为2

    public User() {
    }

    public User(String uid, String phoneNumbers, String username, String signature, String label,
                Integer sex, String email, String birthday, String headSculpture, String background) {
        this.uid = uid;
        this.phoneNumbers = phoneNumbers;
        this.username = username;
        this.signature = signature;
        this.label = label;
        this.sex = sex;
        this.email = email;
        this.birthday = birthday;
        this.headSculpture = headSculpture;
        this.background = background;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }
}
