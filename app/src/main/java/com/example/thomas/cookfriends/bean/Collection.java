package com.example.thomas.cookfriends.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Collection extends BmobObject {
    private String title;
    private String image;
    private String introduction;
    private CookUser cookUser;
    private String userName;
    private String userOnlyId;
    private BmobRelation collected;
    private Integer like_sum = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public CookUser getCookUser() {
        return cookUser;
    }

    public void setCookUser(CookUser cookUser) {
        this.cookUser = cookUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserOnlyId() {
        return userOnlyId;
    }

    public void setUserOnlyId(String userOnlyId) {
        this.userOnlyId = userOnlyId;
    }

    public BmobRelation getCollected() {
        return collected;
    }

    public void setCollected(BmobRelation collected) {
        this.collected = collected;
    }

    public Integer getLike_sum() {
        return like_sum;
    }

    public void setLike_sum(Integer like_sum) {
        this.like_sum = like_sum;
    }
}
