package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.thomas.cookfriends.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class CookSearchActivity extends AppCompatActivity{

    @BindView(R.id.search_view)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setListener();
    }
    private void setListener() {
        // 设置点击键盘上的搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String content) {
                if (content.length() != 0) {
                    Intent intent = new Intent(CookSearchActivity.this, ShowCookeryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("search_key", content);
                    bundle.putString("title", content);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

}
