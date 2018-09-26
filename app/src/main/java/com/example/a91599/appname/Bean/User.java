package com.example.a91599.appname.Bean;

public class User {
    private  String username;
    private int id;
    private String nickname;
    private String mobile;
    private String avatar;
    private int score;
    private String token;
    private int user_id;
    private long createtime;
    private  long expiretime;
    private long expires_in;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public long getExpiretime() {

        return expiretime;
    }

    public void setExpiretime(long expiretime) {
        this.expiretime = expiretime;
    }

    public long getCreatetime() {

        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {

        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getScore() {

        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", score=" + score +
                ", token='" + token + '\'' +
                ", user_id=" + user_id +
                ", createtime=" + createtime +
                ", expiretime=" + expiretime +
                ", expires_in=" + expires_in +
                '}';
    }
}
