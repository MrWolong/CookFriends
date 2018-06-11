package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.HotShareAdapter;
import com.example.thomas.cookfriends.bean.Share;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HotShareActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HotShareAdapter adapter;
    private List<Share> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_share);
        ButterKnife.bind(this);

        initData();
        initListener();
    }

    private void initListener() {

        BmobQuery<Share> query = new BmobQuery();
        query.include("user");
        query.order("-like_sum");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(final List<Share> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        data = list;
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerview.setLayoutManager(layoutManager);
                        adapter = new HotShareAdapter(R.layout.item_hot_share,data);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getApplicationContext(),ShareDetailActivity.class);
                                intent.putExtra("share",data.get(position));
                                startActivity(intent);
                            }
                        });
                        recyclerview.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initData() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
