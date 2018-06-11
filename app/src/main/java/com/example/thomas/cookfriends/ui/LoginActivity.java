package com.example.thomas.cookfriends.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.app.DemoHelper;
import com.example.thomas.cookfriends.bean.CookUser;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail;
    private EditText password;
    private String loginEmailText;
    private String passwordText;
    private TextView signUp;
    private Button signIn;
    private TextView forgetPsw;
    //针对sd卡读取权限申请
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 申请sd卡读取权限
        verifyStoragePermissions(LoginActivity.this);

        // findView
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.password);
        signUp = (TextView) findViewById(R.id.signUp);
        signIn = (Button) findViewById(R.id.signIn);

        // 点击事件
        clickEvents();
    }

    public void clickEvents() {

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailText = loginEmail.getText().toString();
                passwordText = password.getText().toString();
                if (loginEmailText.equals("") || passwordText.equals("")){
                    Toast.makeText(getApplication(), "请填写完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setCancelable(true);
                progressDialog.setMessage("正在登录中...");
                progressDialog.show();
                DemoHelper.getInstance().setCurrentUserName(loginEmailText);
                BmobUser.loginByAccount(loginEmailText, passwordText, new LogInListener<CookUser>() {
                    @Override
                    public void done(CookUser cookUser, BmobException e) {
                        if(e == null){
                            EMClient.getInstance().login(loginEmailText, passwordText, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    if (!LoginActivity.this.isFinishing() && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onError(int i, String s) {
                                }
                                @Override
                                public void onProgress(int i, String s) {
                                    Toast.makeText(getApplication(), "登录失败：", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            if (e.getErrorCode() == 101){
                                Toast.makeText(getApplication(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
    }

    //针对安卓6.0以上机型
    public static void verifyStoragePermissions(Activity activity) {
        // 检查是否有手机sd卡读取权限
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限，则申请，如下
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意权限
        } else {
            //用户拒绝权限
            System.exit(0);
        }
    }
}

