package com.example.thomas.cookfriends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/4/23.
 */

public class Comment extends BmobObject{
    private CookUser author;
    private CookUser reply;
    private String nick;
    private String content;
    private Share share;

    public CookUser getAuthor() {
        return author;
    }

    public void setAuthor(CookUser author) {
        this.author = author;
    }

    public CookUser getReply() {
        return reply;
    }

    public void setReply(CookUser reply) {
        this.reply = reply;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

}
