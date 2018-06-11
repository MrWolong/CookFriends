package com.example.thomas.cookfriends.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CommentAdapter;
import com.example.thomas.cookfriends.adapter.GridAdapter;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.Comment;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.bean.UserLikeNum;
import com.example.thomas.cookfriends.utils.DateTimeUtils;
import com.example.thomas.cookfriends.widget.NineGridView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ShareDetailActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.likes)
    ImageView likes;
    @BindView(R.id.likes_sum)
    TextView likesSum;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.gv_img)
    NineGridView gvImg;
    @BindView(R.id.lv_comment)
    ListView lvComment;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.btn_comment)
    Button btnComment;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.like_icon)
    ImageView likeIcon;
    private Integer sum ;
    private Integer selectedIndex = 0;
    private Share share ;
    private String[] arrayCollection;
    private Comment comment = new Comment();
    private CommentAdapter commentAdapter;
    private List<Comment> data = new ArrayList<>();
    private CookUser reply;
    private CookUser user = BmobUser.getCurrentUser(CookUser.class);
    private CookUser cookUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }
    private void initView() {
        BmobQuery<Share> query = new BmobQuery<Share>();
        query.addWhereRelatedTo("focusShare", new BmobPointer(user));
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        likes.setImageResource(R.drawable.ic_favorite_border);
                        likes.setTag(0);
                    } else {
                        likes.setImageResource(R.drawable.ic_favorite_border);
                        likes.setTag(0);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getObjectId().equals(share.getObjectId())) {
                                likes.setImageResource(R.drawable.ic_favorite_pink);
                                likes.setTag(1);
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        BmobQuery<Share> query2 = new BmobQuery<Share>();
        query2.include("user");
        query2.getObject(share.getObjectId(), new QueryListener<Share>() {

            @Override
            public void done(Share object, BmobException e) {
                if(e==null){
                    sum = object.getLike_sum();
                }else{
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }

        });
        if(user.getUsername().equals(share.getUser().getUsername())){
            likes.setVisibility(View.GONE);
            likesSum.setVisibility(View.GONE);
            tvSum.setVisibility(View.VISIBLE);
            likeIcon.setVisibility(View.VISIBLE);
            // Toolbar作为独立控件进行使用
            toolbar.inflateMenu(R.menu.toolbar_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            deleteShare();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
//            sum = share.getLike_sum();
            tvSum.setText("该分享已收获"+Integer.toString(share.getLike_sum())+"个");
        }else{
            likesSum.setText(Integer.toString(share.getLike_sum()));
        }

        BmobQuery<Collection> query1 = new BmobQuery<Collection>();
        query1.addWhereRelatedTo("myCollection", new BmobPointer(user));
        query1.order("createdAt");
        query1.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if(e == null){
                    arrayCollection = new String[list.size()];
                    for (int i = 0;i<list.size();i++){
                        arrayCollection[i] = list.get(i).getTitle().toString();
                    }
                }

            }
        });
    }

    private void initData() {
        share = (Share) getIntent().getExtras().get("share");
        cookUser = share.getUser();
        Glide.with(getApplicationContext()).load(share.getUser().getAvatar()).into(ivHead);
        tvNickname.setText(share.getUser().getNick());
        tvContent.setText(share.getContent());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(share.getCreatedAt()));
            long millis = calendar.getTimeInMillis();
            tvTime.setText(DateTimeUtils.formatDate(millis));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ;
        //判断是否有图片
        if (share.getImagePaths() != null) {
            gvImg.setVisibility(View.VISIBLE);
            gvImg.setAdapter(new GridAdapter(this, share.getImagePaths()));
        } else {
            gvImg.setVisibility(View.GONE);
        }
        gvImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, share.getImagePaths());
            }
        });
        gvImg.setOnTouchInvalidPositionListener(new NineGridView.OnTouchInvalidPositionListener() {
            @Override
            public boolean onTouchInvalidPosition(int motionEvent) {
                return false;
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share share1 = new Share();
                share1.setObjectId(share.getObjectId());
                BmobRelation relation = new BmobRelation();
                if (likes.getTag().equals(0)) {
                    sum = sum + 1;
                    likesSum.setText(Integer.toString(sum));
                    likes.setImageResource(R.drawable.ic_favorite_pink);
                    likes.setTag(1);
                    relation.add(share1);
                    user.setFocusShare(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplication(), "喜欢成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // 该分享的收藏数量加1
                    Share s = new Share();
                    s.increment("like_sum");
                    s.update(share.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                            } else {
                            }
                        }
                    });
                    UserLikeNum userLikeNum = new UserLikeNum();
                    userLikeNum.increment("likeSum");
                    userLikeNum.update(share.getUser().getUserLikeNum().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "关联成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    sum = sum - 1;
                    likesSum.setText(Integer.toString(sum));
                    likes.setImageResource(R.drawable.ic_favorite_border);
                    likes.setTag(0);
                    // 移除
                    relation.remove(share1);
                    user.setFocusShare(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(), "取消喜欢", Toast.LENGTH_SHORT).show();
                            }else{
                            }
                        }
                    });
                    Share s = new Share();
                    s.increment("like_sum", -1);
                    s.update(share.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                            } else {
                            }
                        }
                    });
                    UserLikeNum userLikeNum = new UserLikeNum();
                    userLikeNum.increment("likeSum",-1);
                    userLikeNum.update(share.getUser().getUserLikeNum().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "关联成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplication(), "关联失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!user.getObjectId().equals(data.get(position).getAuthor().getObjectId())){
                    reply = data.get(position).getAuthor();
                    edtComment.setHint(String.format("回复 %s：", reply.getNick()));
                    Log.w("评论",data.get(position).getAuthor().getNick());
                }else{
                    edtComment.setHint("请输入评论：");
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollection();
            }
        });

        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHomepageActivity.class);
                intent.putExtra("username", share.getUser().getUsername());
                startActivity(intent);
            }
        });

    }
    private void deleteShare() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("确定删除这条分享吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Share share1 = new Share();
                        share1.setObjectId(share.getObjectId());
                        share1.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                }else{
                                    Toast.makeText(getApplicationContext(), "删除失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        alertDialog.show();
    }
    private void addCollection() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("添加至以下哪个收藏？")
                .setSingleChoiceItems(arrayCollection, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                selectedIndex = which;
                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        BmobQuery<Collection> query = new BmobQuery<Collection>();
                        // myCollectio是_User表中的字段，用来存储一个用户所创建的笔记本
                        query.addWhereRelatedTo("myCollection", new BmobPointer(user));
                        query.order("createdAt");
                        query.findObjects(new FindListener<Collection>() {
                            @Override
                            public void done(List<Collection> list, BmobException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        if (i == selectedIndex) {
                                            Share share1 = new Share();
                                            share1.setObjectId(share.getObjectId());
                                            BmobRelation relation = new BmobRelation();
                                            relation.add(share1);
                                            list.get(i).setCollected(relation);
                                            list.get(i).update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Toast.makeText(getApplication(), "添加成功", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getApplication(), "添加失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void imageBrower(int position, List<String> imagePaths) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) imagePaths);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendComment() {
        if(TextUtils.isEmpty(edtComment.getText().toString())){
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        comment.setContent(edtComment.getText().toString());
        comment.setAuthor(user);
        comment.setReply(reply);
        if (comment.getReply() != null) {
            comment.setNick(reply.getNick());
        }
        comment.setShare(share);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "发送成功！", Toast.LENGTH_SHORT).show();
                    edtComment.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "发送失败：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                loadComments();
            }
        });
    }

    private void loadComments() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("share", share);
        query.order("-createdAt");
        query.include("author");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                data = list;
                commentAdapter = new CommentAdapter(getApplicationContext(),list);
                lvComment.setAdapter(commentAdapter);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadComments();
    }


}
