package com.example.thomas.cookfriends.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/4/16.
 */

public class CookUser extends BmobUser{
    private String nick;
    private String avatar;
    private String coverPage;
    private String signature;
    private UserLikeNum userLikeNum;
    private BmobRelation focusShare;
    private BmobRelation focusCollection;
    private BmobRelation myCollection;

    public CookUser(){
    }
    public CookUser(String username){
        setUsername(username);
        this.nick = username;
    }

    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoverPage() {
        return coverPage;
    }

    public void setCoverPage(String coverPage) {
        this.coverPage = coverPage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UserLikeNum getUserLikeNum() {
        return userLikeNum;
    }

    public void setUserLikeNum(UserLikeNum userLikeNum) {
        this.userLikeNum = userLikeNum;
    }

    public BmobRelation getFocusCollection() {
        return focusCollection;
    }

    public void setFocusCollection(BmobRelation focusCollection) {
        this.focusCollection = focusCollection;
    }

    public BmobRelation getFocusShare() {
        return focusShare;
    }

    public void setFocusShare(BmobRelation focusShare) {
        this.focusShare = focusShare;
    }

    public BmobRelation getMyCollection() {
        return myCollection;
    }

    public void setMyCollection(BmobRelation myCollection) {
        this.myCollection = myCollection;
    }

}
