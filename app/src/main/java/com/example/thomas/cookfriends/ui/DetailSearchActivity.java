package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CollectionAdapter;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.Collection;
import com.example.thomas.cookfriends.bean.Share;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class DetailSearchActivity extends AppCompatActivity {

    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.share_button)
    Button shareButton;
    @BindView(R.id.collection_button)
    Button collectionButton;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private int tag;
    private List<Share> shareList = new ArrayList<>();
    private List<Collection> collectionList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    private void initListener() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    Toast.makeText(getApplicationContext(), "查询的关键字不能为空哦", Toast.LENGTH_SHORT).show();
                } else {
                    searchShare(query, true);
                    searchCollection(query, true);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    searchShare(newText, false);
                    searchCollection(newText, false);
                }
                return true;
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = 1;
                if(shareButton.getTag().equals(0)){
                    listview.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.GONE);
                    shareButton.setBackgroundResource(R.drawable.shape_3);
                    shareButton.setTag(1);
                    collectionButton.setBackgroundResource(R.drawable.shape_2);
                    collectionButton.setTag(0);
                }
            }
        });

        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = 2;
                if(collectionButton.getTag().equals(0)){
                    recyclerview.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    shareButton.setBackgroundResource(R.drawable.shape_2);
                    shareButton.setTag(0);
                    collectionButton.setBackgroundResource(R.drawable.shape_3);
                    collectionButton.setTag(1);
                }

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShareDetailActivity.class);
                intent.putExtra("share", shareList.get(position));
                startActivity(intent);
            }
        });

    }

    private void searchCollection(String text, boolean bool) {
        final String inputText = text;
        final boolean inputBool = bool;
        BmobQuery<Collection> query = new BmobQuery();
        query.order("createdAt");
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    collectionList.clear();
                    if (list.size() != 0) {
                        for(Collection collection : list){
                            if(collection.getTitle().contains(inputText)){
                                collectionList.add(collection);
                            }
                        }

                        if(collectionList.size() == 0){
                            if (inputBool == true && tag == 2) {
                                Toast.makeText(getApplicationContext(), "抱歉，查不到相关的收藏夹", Toast.LENGTH_SHORT).show();
                            }
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        recyclerview.setLayoutManager(layoutManager);
                        CollectionAdapter adapter = new CollectionAdapter(R.layout.item_my_collection,collectionList);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getApplicationContext(),CollectionDetailActivity.class);
                                intent.putExtra("objectId",collectionList.get(position).getObjectId());
                                startActivity(intent);
                            }
                        });
                        recyclerview.setAdapter(adapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "抱歉，查不到相关的收藏夹", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchShare(String text, boolean bool) {
        final String inputText = text;
        final boolean inputBool = bool;
        BmobQuery<Share> query = new BmobQuery<Share>();
        query.include("user");
        query.order("-createdAt");
        //本来是可以直接使用bmob的模糊查询的，但是要付费，所以只能另辟蹊径
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        if (inputBool == true && tag == 1) {
                            Toast.makeText(getApplicationContext(), "抱歉，查不到相关的分享", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        shareList.clear();
                        for(Share share : list){
                            if(share.getContent().contains(inputText)){
                                shareList.add(share);
                            }
                        }
                        if (shareList.size() == 0) {
                            if (inputBool == true && tag == 1) {
                                Toast.makeText(getApplicationContext(), "抱歉，查不到相关的分享", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ShareFoodAdapter shareFoodAdapter = new ShareFoodAdapter(getApplication(),shareList);
                            listview.setAdapter(shareFoodAdapter);
                        }
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        shareButton.setTag(1);
        collectionButton.setTag(0);
    }

}
