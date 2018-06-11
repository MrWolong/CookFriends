package com.example.thomas.cookfriends.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CollectionAdapter;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.app.Constant;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserHomepageActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bgImg)
    ImageView bgImg;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.intro)
    TextView intro;
    @BindView(R.id.call)
    Button call;
    @BindView(R.id.user_share)
    Button userShare;
    @BindView(R.id.user_collection)
    Button userCollection;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String username;
    private CookUser mUser;
    private ShareFoodAdapter shareFoodAdapter;
    private CollectionAdapter collectionAdapter;
    private CookUser user = BmobUser.getCurrentUser(CookUser.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        username = getIntent().getStringExtra("username");
        BmobQuery<CookUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(new FindListener<CookUser>() {
            @Override
            public void done(List<CookUser> list, BmobException e) {
                mUser = list.get(0);
                Glide.with(getApplicationContext()).load(mUser.getAvatar()).into(avatar);
                Glide.with(getApplicationContext()).load(mUser.getCoverPage()).into(bgImg);
                name.setText(mUser.getNick());
                if (mUser.getSignature() == null || mUser.getSignature().equals("")) {
                    intro.setText("简介：这个人很懒，什么也没留下...");
                }else{
                    intro.setText("简介："+mUser.getSignature() );
                }
                BmobQuery<Share> query2 = new BmobQuery();
                query2.addWhereEqualTo("user", mUser);  // 查询当前用户的所有语录
                query2.include("user");
                query2.order("-createdAt");
                query2.findObjects(new FindListener<Share>() {
                    @Override
                    public void done(List<Share> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 0) {
                                shareFoodAdapter = new ShareFoodAdapter(getApplicationContext(),list);
                                listview.setAdapter(shareFoodAdapter);
                                setListViewHeightBasedOnChildren();
                                listview.setVisibility(View.VISIBLE);
                            } else
                                Toast.makeText(getApplicationContext(), "该用户还未发表任何分享哦", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "分享列表查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                BmobQuery<Collection> query3 = new BmobQuery();
                query3.addWhereEqualTo("cookUser", mUser);
                query3.order("createdAt");
                query3.findObjects(new FindListener<Collection>() {
                    @Override
                    public void done(final List<Collection> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 0) {

                                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                recyclerview.setLayoutManager(layoutManager);
                                collectionAdapter = new CollectionAdapter(R.layout.item_my_collection,list);
                                collectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        Intent intent = new Intent(getApplicationContext(),CollectionDetailActivity.class);
                                        intent.putExtra("objectId",list.get(position).getObjectId());
                                        startActivity(intent);
                                    }
                                });
                                recyclerview.setAdapter(collectionAdapter);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "笔记本查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void initView() {
        if(user.getUsername().equals(username)){
            call.setVisibility(View.GONE);
        }
        userShare.setTag(1);
        userCollection.setTag(0);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ChatActivity.class);
                intent.putExtra(Constant.EXTRA_USER_ID, username);
                startActivity(intent);
            }
        });

        userShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userShare.getTag().equals(0)) {
                    userShare.setBackgroundResource(R.drawable.shape_3);
                    userCollection.setBackgroundResource(R.drawable.shape_2);
                    userShare.setTag(1);
                    userCollection.setTag(0);
                    recyclerview.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
            }
        });

        userCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCollection.getTag().equals(0)) {
                    userCollection.setBackgroundResource(R.drawable.shape_3);
                    userShare.setBackgroundResource(R.drawable.shape_2);
                    userCollection.setTag(1);
                    userShare.setTag(0);
                    listview.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //动态修改listview高度，使得listview能完全展开
    private void setListViewHeightBasedOnChildren() {
        if (listview == null) {
            return;
        }
        if (shareFoodAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //Toast.makeText(getApplication(), Integer.toString(simpleAdapter.getCount()), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < shareFoodAdapter.getCount(); i++) {
            View listItem = shareFoodAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (shareFoodAdapter.getCount() - 1));
        listview.setLayoutParams(params);
    }
}

