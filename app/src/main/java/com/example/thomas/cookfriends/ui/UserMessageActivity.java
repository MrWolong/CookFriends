package com.example.thomas.cookfriends.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.CookUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserMessageActivity extends AppCompatActivity {

    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.rl_nick)
    RelativeLayout rlNick;
    @BindView(R.id.rl_intro)
    RelativeLayout rlIntro;
    @BindView(R.id.rl_username)
    RelativeLayout rlUsername;
    @BindView(R.id.rl_coverpage)
    RelativeLayout rlCoverpage;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_coverpage)
    ImageView ivCoverpage;
    @BindView(R.id.progress)
    ProgressBar progress;

    private CookUser cookUser;
    private AlertDialog.Builder alertDialog;

    //关于图片选择并裁剪，请求识别码
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int RESULT_REQUEST = 0xa3;
    //裁剪图片后的宽(X)和高(Y)
    private static int output_X = 200;
    private static int output_Y = 200;
    private static String img_uri = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        cookUser = BmobUser.getCurrentUser(CookUser.class);
        Glide.with(this).load(cookUser.getAvatar()).into(ivAvatar);
        Glide.with(this).load(cookUser.getCoverPage()).into(ivCoverpage);
        tvNick.setText(cookUser.getNick());
        tvIntro.setText(cookUser.getSignature());
        tvUsername.setText(cookUser.getUsername());
    }

    @OnClick({R.id.rl_avatar, R.id.rl_nick, R.id.rl_intro, R.id.rl_username, R.id.rl_coverpage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, CODE_GALLERY_REQUEST);
                break;
            case R.id.rl_nick:
                changeNick();
                alertDialog.show();
                break;
            case R.id.rl_intro:
                changeIntro();
                alertDialog.show();
                break;
            case R.id.rl_coverpage:
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, REQUEST);
                break;
        }
    }

    private void changeIntro() {
        final EditText editText = new EditText(this);
        editText.setText(cookUser.getSignature());
        editText.setSingleLine(true);
        //先设置好点击“修改昵称”弹出的dialog
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String input = editText.getText().toString();
                            // 修改后端的用户昵称
                            cookUser.setSignature(input);
                            cookUser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        tvIntro.setText(input);
                                        Toast.makeText(getApplication(), "简介更换成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getApplication(), "简介更换失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                })
                .setNegativeButton("取消", null).create();
    }

    private void changeNick() {
        final EditText editText = new EditText(this);
        editText.setText(cookUser.getNick());
        editText.setSingleLine(true);
        //先设置好点击“修改昵称”弹出的dialog
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String input = editText.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "昵称不能为空哦", Toast.LENGTH_SHORT).show();
                        } else {
                            // 修改后端的用户昵称
                            cookUser.setNick(input);
                            cookUser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        tvNick.setText(input);
                                        Toast.makeText(getApplication(), "昵称更换成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getApplication(), "昵称更换失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                })
                .setNegativeButton("取消", null).create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                if (data != null) {
                    cropRawPhoto(data.getData());
                }
                break;
            case CODE_RESULT_REQUEST:
                if (data != null) {
                    setImageToAvatar(data);
                }
                break;
            case REQUEST:
                if (data != null) {
                    cropRawCoverPage(data.getData());
                }
                break;
            case RESULT_REQUEST:
                if (data != null) {
                    setImageToCoverPage(data);
                }
            default:
                break;
        }
    }
    public void uploadAvatar(final String uri) {
        final BmobFile file = new BmobFile(new File(uri));
        progress.setVisibility(View.VISIBLE);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
                    cookUser.setAvatar(file.getFileUrl());
                    cookUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //上传图片
                                Glide.with(getApplicationContext()).load(file.getFileUrl()).into(ivAvatar);
                                Toast.makeText(getApplication(), "头像更换成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "头像更换失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }


    public void uploadCoverPage(final String uri) {
        final BmobFile file = new BmobFile(new File(uri));
        progress.setVisibility(View.VISIBLE);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
                    cookUser.setCoverPage(file.getFileUrl());
                    cookUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //上传图片
                                Glide.with(getApplicationContext()).load(file.getFileUrl()).into(ivCoverpage);
                                Toast.makeText(getApplication(), "个人主页封面更换成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "个人主页封面更换失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void cropRawCoverPage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据
     */
    private void setImageToCoverPage(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            //提取图片数据，将头像的View设置为自定义图片
            Bitmap photo = extras.getParcelable("data");
            //user_img.setImageBitmap(photo);
            //创建文件夹，储存截好的头像，方便下次打开的时候读取
            File newfile = new File(Environment.getExternalStorageDirectory(), "Pic");
            if (!newfile.exists()) {
                newfile.mkdir();
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/Pic", "head.jpg");
            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                try {
                    out.flush();
                    img_uri = Environment.getExternalStorageDirectory() + "/Pic/head.jpg";
                    //将路径转为bitmap然后显示出来
                    uploadCoverPage(img_uri);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }


    private void setImageToAvatar(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            //提取图片数据，将头像的View设置为自定义图片
            Bitmap photo = extras.getParcelable("data");
            //user_img.setImageBitmap(photo);
            //创建文件夹，储存截好的头像，方便下次打开的时候读取
            File newfile = new File(Environment.getExternalStorageDirectory(), "Pic");
            if (!newfile.exists()) {
                newfile.mkdir();
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/Pic", "head.jpg");
            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                try {
                    out.flush();
                    img_uri = Environment.getExternalStorageDirectory() + "/Pic/head.jpg";
                    //将路径转为bitmap然后显示出来
                    uploadAvatar(img_uri);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
