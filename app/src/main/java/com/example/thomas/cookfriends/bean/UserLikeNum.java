package com.example.thomas.cookfriends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/4/29.
 */

public class UserLikeNum extends BmobObject{

    private CookUser user;
    private Integer likeSum = 0;

    public CookUser getUser() {
        return user;
    }

    public void setUser(CookUser user) {
        this.user = user;
    }

    public Integer getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(Integer likeSum) {
        this.likeSum = likeSum;
    }
}
