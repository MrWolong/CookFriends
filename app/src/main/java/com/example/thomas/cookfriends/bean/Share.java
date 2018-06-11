package com.example.thomas.cookfriends.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2018/4/18.
 */

public class Share extends BmobObject implements Serializable {
    private String content;
    private List<String> imagePaths;
    private CookUser user;
    private Integer like_sum = 0;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public CookUser getUser() {
        return user;
    }

    public void setUser(CookUser user) {
        this.user = user;
    }

    public Integer getLike_sum() {
        return like_sum;
    }

    public void setLike_sum(Integer like_sum) {
        this.like_sum = like_sum;
    }
}
