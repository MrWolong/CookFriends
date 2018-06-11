package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CollectionAdapter;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MyLikeActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.user_share)
    Button userShare;
    @BindView(R.id.user_collection)
    Button userCollection;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private ShareFoodAdapter shareFoodAdapter;
    private CollectionAdapter adapter;
    private List<Share> shareList;
    private CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initView() {
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ShareDetailActivity.class);
                intent.putExtra("share",shareList.get(position));
                startActivity(intent);
            }
        });

    }

    private void initData() {
         BmobQuery<Share> query = new BmobQuery();
        query.addWhereRelatedTo("focusShare", new BmobPointer(cookUser));
        query.include("user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        shareList = list;
                        shareFoodAdapter = new ShareFoodAdapter(getApplicationContext(),list);
                        listview.setAdapter(shareFoodAdapter);
                    } else
                        Toast.makeText(getApplicationContext(), "你还未收藏任何分享哦", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "分享列表查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BmobQuery<Collection> query2 = new BmobQuery();
        query2.addWhereRelatedTo("focusCollection", new BmobPointer(cookUser));
        query2.order("createdAt");
        query2.findObjects(new FindListener<Collection>() {
            @Override
            public void done(final List<Collection> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        recyclerview.setLayoutManager(layoutManager);
                        adapter = new CollectionAdapter(R.layout.item_my_collection,list);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getApplicationContext(),CollectionDetailActivity.class);
                                intent.putExtra("objectId",list.get(position).getObjectId());
                                startActivity(intent);
                            }
                        });
                        recyclerview.setAdapter(adapter);
                    }
                }
            }
        });
    }

}
