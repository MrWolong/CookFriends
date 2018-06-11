package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CollectionAdapter;
import com.example.thomas.cookfriends.bean.Collection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HotCollectionActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private CollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();

    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        add.setVisibility(View.GONE);
        title.setText("热门收藏夹");
    }

    private void initData() {
        BmobQuery<Collection> query = new BmobQuery();
        query.order("-like_sum");
        query.findObjects(new FindListener<Collection>() {
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
                } else {
                    Toast.makeText(getApplicationContext(), "收藏夹查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
