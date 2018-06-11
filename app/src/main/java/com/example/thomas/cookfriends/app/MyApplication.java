package com.example.thomas.cookfriends.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
        DemoHelper.getInstance().init(getApplicationContext());
    }
    private void initBmob() {
        Bmob.initialize(this,"9ea906ba441c63ca603baee4b1bca52b");
    }

}
