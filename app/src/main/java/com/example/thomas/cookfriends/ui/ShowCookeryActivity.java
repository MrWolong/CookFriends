package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CookItemAdapter;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;
import com.example.thomas.cookfriends.db.CooksDBManager;
import com.example.thomas.cookfriends.utils.NetworkUtil;
import com.example.thomas.cookfriends.utils.OkHttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ShowCookeryActivity extends AppCompatActivity {

    @BindView(R.id.cookery_back)
    ImageView ivBack;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rv_cookery)
    RecyclerView rvCookery;

    private CookItemAdapter cookItemAdapter;
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();
    private int cookId;
    private int pn = 0;
    private String title;
    private String search_key;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cook);
        ButterKnife.bind(this);
        cookItemAdapter = new CookItemAdapter(R.layout.item_cooks_list, info);
        rvCookery.setLayoutManager(new LinearLayoutManager(ShowCookeryActivity.this));
        rvCookery.setAdapter(cookItemAdapter);

        initData();
        setListener();
        requestHttpData();
    }

    private void setListener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCookeryActivity.this, CookSearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String search = bundle.getString("title");
        tvSearch.setText(search);

        search_key = bundle.getString("search_key");
        cookId = bundle.getInt("id", 0);//cid

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onFailure: ");

        //requestHttpData();
    }

    private void requestHttpData() {
        OkHttpUtil.sendOkHttpRequest(NetworkUtil.getURL(cookId, search_key, pn, 10), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseDataWithGson(responseData);
                if (info.size() >= 10) {
                    Log.i(TAG, "onResume:" + info.size());
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        Log.i(TAG, "loadMoreData: " + cookItemAdapter);
        cookItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvCookery.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pn += 10;
                        requestHttpData();
                    }
                }, 2000);
            }
        }, rvCookery);
    }

    private void parseDataWithGson(final String responseData) {
        Gson gson = new Gson();
        final ShowCookersInfo loadInfo = gson.fromJson(responseData, ShowCookersInfo.class);
        Log.i(TAG, "loadInfo: " + loadInfo.toString());
        Log.i(TAG, "loadInfo.getResult: " + loadInfo.getResult().toString());
        Log.i(TAG, "info: " + info.toString());
        info.addAll(loadInfo.getResult().getData());
        Log.i(TAG, "info: " + info.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cookItemAdapter.setNewData(info);
                cookItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Log.i("mycook", info.get(position).getAlbums().get(0));
                        CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).setData(info.get(position));
                        CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).insertData(info.get(position));
                        Intent intent = new Intent(ShowCookeryActivity.this, CookDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
