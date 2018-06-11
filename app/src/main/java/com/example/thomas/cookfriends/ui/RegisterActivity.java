package com.example.thomas.cookfriends.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.app.DemoHelper;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.UserLikeNum;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private EditText nickName;
    private EditText loginEmail;
    private EditText password;
    private EditText psw_again;
    private String nickNameText;
    private String loginEmailText;
    private String passwordText;
    private String psw_againText;
    private Button signUp;
    public static final String IMG = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513940076438&di=f9f4f0e1b7ef6c51c7304a1a56db02d3&imgtype=0&src=http%3A%2F%2Fimg71.nipic.com%2Ffile%2F20160306%2F10504359_163227658431_1.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nickName = (EditText) findViewById(R.id.nickName);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.password);
        psw_again = (EditText) findViewById(R.id.psw_again);
        signUp = (Button) findViewById(R.id.signUp);
        clickEvents();
    }

    public void clickEvents() {

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickNameText = nickName.getText().toString();
                loginEmailText = loginEmail.getText().toString();
                passwordText = password.getText().toString();
                psw_againText = psw_again.getText().toString();
                if (nickNameText.equals("") || loginEmailText.equals("") || passwordText.equals("") || psw_againText.equals(""))
                    Toast.makeText(getApplication(), "请填写完整", Toast.LENGTH_SHORT).show();
                else if (!passwordText.equals(psw_againText))
                    Toast.makeText(getApplication(), "请注意：您的两次密码填写不一致", Toast.LENGTH_SHORT).show();
                else {
                    signUp();
                }
            }
        });

    }
    public void createDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("注册成功！尝试登录吧")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        alertDialog.show();
    }
    public void signUp() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("正在注册中...");
        progressDialog.show();
        new Thread(new Runnable() {
            public void run(){
                try {
                    EMClient.getInstance().createAccount(loginEmailText, passwordText);
                    CookUser user = new CookUser();
                    user.setNick(nickNameText);
                    user.setPassword(passwordText);
                    user.setUsername(loginEmailText);
                    user.setAvatar(IMG);
                    user.signUp(new SaveListener<CookUser>() {
                        @Override
                        public void done(CookUser s, BmobException e) {
                            if (e == null) {
                                final CookUser user = new CookUser();
                                user.setUsername(loginEmailText);
                                user.setPassword(passwordText);
                                user.login(new SaveListener<CookUser>() {
                                    @Override
                                    public void done(CookUser s, BmobException e) {
                                        if (e == null) {
                                            UserLikeNum userLikeNum = new UserLikeNum();
                                            userLikeNum.setUser(BmobUser.getCurrentUser(CookUser.class));
                                            userLikeNum.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if(e == null){
                                                        UserLikeNum likeNum =new UserLikeNum();
                                                        likeNum.setObjectId(s);
                                                        CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
                                                        cookUser.setUserLikeNum(likeNum);
                                                        cookUser.update(new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                            Collection collection = new Collection();
                                            collection.setUserOnlyId(s.getObjectId());
                                            collection.setTitle("默认收藏夹");
                                            collection.setIntroduction("将你喜欢的美食添加至收藏夹之中~");
                                            collection.setImage(user.getAvatar());
                                            collection.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e == null) {
                                                        Collection tempp = new Collection();
                                                        tempp.setObjectId(s);
                                                        BmobRelation relation = new BmobRelation();
                                                        relation.add(tempp);
                                                        CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
                                                        cookUser.setMyCollection(relation);// 添加一对多关联
                                                        cookUser.update(new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    //Toast.makeText(getApplication(), "关联成功", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    //Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        //Toast.makeText(getApplication(), "创建成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(getApplication(), "创建失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            //Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!RegisterActivity.this.isFinishing())
                                            progressDialog.dismiss();
                                        DemoHelper.getInstance().setCurrentUserName(loginEmailText);
                                        createDialog();
                                    }
                                });
                            } else {
                                if (e.getErrorCode() == 202 || e.getErrorCode() == 203) {
                                    Toast.makeText(getApplication(), "抱歉，该账号已被注册", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplication(), "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });
                }catch (final HyphenateException e){
                }
            }
        }).start();
    }

}

