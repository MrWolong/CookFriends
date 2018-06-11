package com.example.thomas.cookfriends.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.PagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.page)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initListener();
    }

    private void initFragment() {
        tabLayout.addTab(tabLayout .newTab().setText("首页").setIcon(R.drawable.ic_home_white));
        tabLayout.addTab(tabLayout.newTab().setText("邻家厨房").setIcon(R.drawable.ic_data_usage_white));
        tabLayout.addTab(tabLayout.newTab().setText("消息").setIcon(R.drawable.ic_data_usage_white));
        tabLayout.addTab(tabLayout.newTab().setText("发现").setIcon(R.drawable.ic_data_usage_white));
        tabLayout.addTab(tabLayout.newTab().setText("我的").setIcon(R.drawable.ic_person_white));
        // 修改样式，主要是调近了图标与文字之间的距离
        tabLayout.getTabAt(0).setCustomView(getTabView("首页", R.drawable.ic_home_white));
        tabLayout.getTabAt(1).setCustomView(getTabView("邻家厨房", R.drawable.ic_data_usage_white));
        tabLayout.getTabAt(2).setCustomView(getTabView("消息", R.drawable.ic_data_usage_white));
        tabLayout.getTabAt(3).setCustomView(getTabView("发现", R.drawable.ic_data_usage_white));
        tabLayout.getTabAt(4).setCustomView(getTabView("我的", R.drawable.ic_person_white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        changeTabSelect(tabLayout.getTabAt(0));//打开APP时停留在第一页，故先改变其颜色
        viewPager.setOffscreenPageLimit(5);//取消预加载
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                changeTabSelect(tab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Tab自定义view
    public View getTabView(String title, int src) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tab_view, null);
        TextView textView = view.findViewById(R.id.textview);
        textView.setText(title);
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setImageResource(src);
        return view;
    }
    // 切换颜色
    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = view.findViewById(R.id.imageview);
        TextView txt_title = view.findViewById(R.id.textview);
        txt_title.setTextColor(getResources().getColor(R.color.colorBase1));
        if (txt_title.getText().toString().equals("首页")) {
            img_title.setImageResource(R.drawable.ic_home_green);
        } else if (txt_title.getText().toString().equals("邻家厨房")) {
            img_title.setImageResource(R.drawable.ic_data_usage_green);
        } else if (txt_title.getText().toString().equals("消息")) {
            img_title.setImageResource(R.drawable.ic_data_usage_green);
        } else if (txt_title.getText().toString().equals("发现")) {
            img_title.setImageResource(R.drawable.ic_data_usage_green);
        }  else if (txt_title.getText().toString().equals("我的")) {
            img_title.setImageResource(R.drawable.ic_person_green);
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.imageview);
        TextView txt_title = (TextView) view.findViewById(R.id.textview);
        txt_title.setTextColor(getResources().getColor(R.color.colorBackground));
        if (txt_title.getText().toString().equals("首页")) {
            img_title.setImageResource(R.drawable.ic_home_white);
        } else if (txt_title.getText().toString().equals("邻家厨房")) {
            img_title.setImageResource(R.drawable.ic_data_usage_white);
        } else if (txt_title.getText().toString().equals("消息")) {
            img_title.setImageResource(R.drawable.ic_data_usage_white);
        } else if (txt_title.getText().toString().equals("发现")) {
            img_title.setImageResource(R.drawable.ic_data_usage_white);
        }  else if (txt_title.getText().toString().equals("我的")) {
            img_title.setImageResource(R.drawable.ic_person_white);
        }
    }



}
