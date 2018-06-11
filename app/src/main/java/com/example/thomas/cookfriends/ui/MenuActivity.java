package com.example.thomas.cookfriends.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.MenuListAdapter;
import com.example.thomas.cookfriends.bean.cookbean.SortTagInfo;
import com.example.thomas.cookfriends.widget.IndexView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.index_view)
    IndexView indexView;
    @BindView(R.id.tv_classify_title)
    TextView tvTitle;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.lv_classify)
    ListView lvClassify;
    @BindView(R.id.tv_classify_toast)
    TextView tvClassifyToast;
    @BindView(R.id.rl_classify)
    RelativeLayout rlClassify;
    private SortTagInfo sortTagInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        initData();
        initListener();

    }

    private void initListener() {
        MenuListAdapter menuListAdapter = new MenuListAdapter(this,sortTagInfo);
        lvClassify.setAdapter(menuListAdapter);
        indexView.setOnLetterChangeListener(new IndexView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(int selectedIndex) {
                lvClassify.setSelection(selectedIndex - 1);
                tvClassifyToast.setText(sortTagInfo.getResult().get(selectedIndex - 1).getName());//设置中间显示的字
                tvClassifyToast.setVisibility(View.VISIBLE);//设置为可见
            }

            @Override
            public void onClickUp() {
                tvClassifyToast.setVisibility(View.GONE);//当放开时，设置为不可见
            }
        });
        lvClassify.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View v = view.getChildAt(0);
                if (v == null) {
                    return;
                }
                tvTitle.setText(sortTagInfo.getResult().get(firstVisibleItem).getName());
                indexView.setSelected(firstVisibleItem + 1);
            }
        });
    }
    private void initData() {
        Gson gson = new Gson();
        try {
            InputStream in = this.getAssets().open("cooks_classify");
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String len;
            while ((len = reader.readLine()) != null) {
                builder.append(len);
            }
            reader.close();
            sortTagInfo = gson.fromJson(builder.toString(), SortTagInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
