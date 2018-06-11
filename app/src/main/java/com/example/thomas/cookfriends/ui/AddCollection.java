package com.example.thomas.cookfriends.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class AddCollection extends AppCompatActivity {


    //关于图片选择并裁剪，请求识别码
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    //裁剪图片后的宽(X)和高(Y)
    private static int output_X = 200;
    private static int output_Y = 200;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.introduction)
    EditText introduction;

    private CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
    private static String img_uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);
        ButterKnife.bind(this);
        initListener();
    }


    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CODE_GALLERY_REQUEST);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "收藏夹标题不能为空哦", Toast.LENGTH_SHORT).show();
                else if (img_uri == null)
                    Toast.makeText(getApplicationContext(), "请上传收藏夹封面图片", Toast.LENGTH_SHORT).show();
                else
                    uploadImage_then_create(img_uri);
            }
        });
    }

    /**
     * 裁剪原始的图片
     */
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

    /**
     * 提取保存裁剪之后的图片数据
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            File newfile = new File(Environment.getExternalStorageDirectory(), "Pic");
            if (!newfile.exists()) {
                newfile.mkdir();
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/Pic", "bookcover.jpg");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                try {
                    out.flush();
                    img_uri = Environment.getExternalStorageDirectory() + "/Pic/bookcover.jpg";
                    Bitmap bitmap = BitmapFactory.decodeFile(img_uri);
                    image.setImageBitmap(bitmap);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void uploadImage_then_create(String uri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("正在创建中...");
        progressDialog.show();
        final BmobFile file = new BmobFile(new File(uri));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Collection collection = new Collection();
                    collection.setCookUser(cookUser);
                    collection.setUserName(cookUser.getUsername());
                    collection.setUserOnlyId(cookUser.getObjectId());
                    collection.setTitle(name.getText().toString());
                    collection.setIntroduction(introduction.getText().toString());
                    collection.setImage(file.getFileUrl());
                    collection.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Collection temp = new Collection();
                                temp.setObjectId(s);
                                BmobRelation relation = new BmobRelation();
                                relation.add(temp);
                                cookUser.setMyCollection(relation);
                                cookUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "收藏夹创建成功", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            Intent intent = new Intent();
                                            setResult(0, intent);
                                            finish();
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        } else {
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplication(), "图片上传失败", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
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
                    setImageToHeadView(data);
                }
                break;
            default:
                break;
        }
    }
}
