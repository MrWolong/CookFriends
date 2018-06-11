package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.CookUser;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;


public class WelcomeActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        CookUser current_user = BmobUser.getCurrentUser(CookUser.class);
        if (current_user != null) {
            intent = new Intent(this, MainActivity.class);
            finish();
        } else {
            // 不存在当前用户，转向登录界面
            intent = new Intent(this, LoginActivity.class);
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        };
        timer.schedule(task, 1000 * 2);
    }
}
