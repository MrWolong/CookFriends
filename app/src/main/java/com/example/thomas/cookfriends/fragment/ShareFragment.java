package com.example.thomas.cookfriends.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.ui.ShareActivity;
import com.example.thomas.cookfriends.ui.ShareDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ShareFragment extends Fragment {
    @BindView(R.id.lv_share)
    ListView lvshare;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;
    private ShareFoodAdapter shareFoodAdapter;
    private List<Share> shareList;
    private CookUser cookUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_share, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initData();
        initListener();
        return contentView;
    }

    private void initData() {
        BmobQuery<Share> bmobQuery = new BmobQuery<>();
        bmobQuery.include("user");
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if(e == null){
                    shareList = list;
                    shareFoodAdapter = new ShareFoodAdapter(getContext(),list);
                    lvshare.setAdapter(shareFoodAdapter);
                }
            }
        });
    }

    private void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
            }
        });
        lvshare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShareDetailActivity.class);
                intent.putExtra("share", shareList.get(position));
                startActivity(intent);
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        refresh.setRefreshing(false);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "刷新完毕", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 2000);

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        cookUser = BmobUser.getCurrentUser(CookUser.class);
        initData();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
