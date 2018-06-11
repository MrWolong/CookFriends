package com.example.thomas.cookfriends.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.GridImageAdapter;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.widget.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ShareActivity extends AppCompatActivity implements GridImageAdapter.onAddPicClickListener {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private GridImageAdapter adapter;
    List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        //1 this 上下文环境context  2 this onAddPicClickListener的实现类对象
        adapter = new GridImageAdapter(this, this);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recycler.setAdapter(adapter);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_send:
                sendAction();
                break;
        }
    }

    private void sendAction() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("正在发布中...");
        progressDialog.show();
        final Share share = new Share();
        share.setUser(BmobUser.getCurrentUser(CookUser.class));
        share.setContent(etContent.getText().toString());

        //判断是否有图片
        if (selectList != null && selectList.size() > 0) {
            String[] imagePaths = new String[selectList.size()];
            for (int i = 0; i < selectList.size(); i++) {
                imagePaths[i] = selectList.get(i).getPath();
            }
            //批量上传图片
            BmobFile.uploadBatch(imagePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list.size() == selectList.size()) {
                        share.setImagePaths(list1);
                        send(share);
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(ShareActivity.this, "发送失败：" + s, Toast.LENGTH_SHORT)
                            .show();
                    progressDialog.dismiss();
                }
            });
        } else {
            send(share);
            progressDialog.dismiss();
        }

    }

    private void send(Share share) {
        share.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(ShareActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ShareActivity.this, "发送失败:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onAddPicClick() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(ShareActivity.this)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(
                        PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                //.compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                //.compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("resultresult", "ERonActivityResultR");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();

                    Log.i("resultresult", "onActivityResult:" + selectList.size());
                    break;
            }
        } else {
            Log.i("resultresult", "ERR");
        }
    }
}
