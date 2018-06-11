package com.example.thomas.cookfriends.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.HotAdapter;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.Share;
import com.example.thomas.cookfriends.ui.DetailSearchActivity;
import com.example.thomas.cookfriends.ui.ShareDetailActivity;
import com.example.thomas.cookfriends.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class FindFragment extends Fragment {
    @BindView(R.id.search_button)
    Button search;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.lv_share)
    ListView listview;
    @BindView(R.id.scroll)
    ScrollView scroll;
    Unbinder unbinder;
    private List<Integer> data = new ArrayList<>();
    private HotAdapter adapter;
    private ShareFoodAdapter shareFoodAdapter;
    private List<Share> shareList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_find, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initData();
        initView();
        initListener();
        return contentView;
    }

    private void initData() {
        data.add(R.drawable.hot_user_img);
        data.add(R.drawable.hot_cook_img);
        data.add(R.drawable.hot_collection_img);

        BmobQuery<Share> bmobQuery = new BmobQuery<>();
        bmobQuery.include("user");
        bmobQuery.order("-like_sum");
        bmobQuery.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if(e == null){
                    shareList = list;
                    shareFoodAdapter = new ShareFoodAdapter(getContext(),list);
                    listview.setAdapter(shareFoodAdapter);
                    setListViewHeightBasedOnChildren();
                }
            }
        });

    }

    private void initView() {
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerview.setLayoutManager(fullyGridLayoutManager);
        adapter = new HotAdapter(data);
        recyclerview.setAdapter(adapter);

    }

    private void initListener() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailSearchActivity.class);
                startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShareDetailActivity.class);
                intent.putExtra("share", shareList.get(position));
                startActivity(intent);
            }
        });
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
