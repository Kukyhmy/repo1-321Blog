package com.kuky.blog.basics.entity;

import lombok.Data;

@Data
public class Userconnection {
    private String userid;

    private String providerid;

    private String provideruserid;

    private Integer rank;

    private String displayname;

    private String profileurl;

    private String imageurl;

    private String accesstoken;

    private String secret;

    private String refreshtoken;

    private Long expiretime;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid == null ? null : providerid.trim();
    }

    public String getProvideruserid() {
        return provideruserid;
    }

    public void setProvideruserid(String provideruserid) {
        this.provideruserid = provideruserid == null ? null : provideruserid.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname == null ? null : displayname.trim();
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl == null ? null : profileurl.trim();
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken == null ? null : accesstoken.trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken == null ? null : refreshtoken.trim();
    }

    public Long getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Long expiretime) {
        this.expiretime = expiretime;
    }
}