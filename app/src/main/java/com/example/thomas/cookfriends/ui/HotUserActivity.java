package com.example.thomas.cookfriends.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.HotUserAdapter;
import com.example.thomas.cookfriends.bean.UserLikeNum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HotUserActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private HotUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_user);
        ButterKnife.bind(this);

        initData();
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

    private void initData() {
        BmobQuery<UserLikeNum> query = new BmobQuery<>();
        query.include("user");
        query.order("-likeSum");
        query.findObjects(new FindListener<UserLikeNum>() {
            @Override
            public void done(List<UserLikeNum> list, BmobException e) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerview.setLayoutManager(linearLayoutManager);
                adapter = new HotUserAdapter(getApplicationContext(),list);
                recyclerview.setAdapter(adapter);
            }
        });
    }

}
