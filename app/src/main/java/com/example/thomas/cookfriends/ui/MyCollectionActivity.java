package com.example.thomas.cookfriends.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CollectionAdapter;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.CookUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MyCollectionActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);
    private CollectionAdapter adapter;
    private List<Collection> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        BmobQuery<Collection> query = new BmobQuery();
        query.addWhereEqualTo("userOnlyId", cookUser.getObjectId());
        query.order("createdAt");
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        data = list;
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new CollectionAdapter(R.layout.item_my_collection,data);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getApplicationContext(),CollectionDetailActivity.class);
                                intent.putExtra("objectId",data.get(position).getObjectId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else{
                        Toast.makeText(getApplicationContext(), "快去创建收藏夹吧~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddCollection.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                initData();
                break;
            default:
                break;
        }
    }

}
