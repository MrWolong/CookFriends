package com.example.thomas.cookfriends.fragment;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.CategoryAdapter;
import com.example.thomas.cookfriends.adapter.CookAdapter;
import com.example.thomas.cookfriends.bean.cookbean.Category;
import com.example.thomas.cookfriends.bean.cookbean.ShowCookersInfo;
import com.example.thomas.cookfriends.db.CooksDBManager;
import com.example.thomas.cookfriends.ui.CookDetailActivity;
import com.example.thomas.cookfriends.ui.CookSearchActivity;
import com.example.thomas.cookfriends.ui.MenuActivity;
import com.example.thomas.cookfriends.ui.ShowCookeryActivity;
import com.example.thomas.cookfriends.utils.NetworkUtil;
import com.example.thomas.cookfriends.utils.OkHttpUtil;
import com.example.thomas.cookfriends.widget.MyGridView;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_more_category)
    TextView tvMoreCategory;
    @BindView(R.id.gv_category)
    MyGridView gvCategory;
    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;
    Unbinder unbinder;
    private List<String> imagePaths;
    private List<String> titles;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> cookList = new ArrayList<>();
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CookAdapter cookAdapter;
    private int pn = 0;
    private String search_key;
    private int cookId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initData();
        initView();
        initListener();
        return contentView;
    }
    private void initData() {
        imagePaths = new ArrayList<>();
        imagePaths.add("http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/t/0/7_583507.jpg");
        imagePaths.add("http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/t/6/5196_525400.jpg");
        imagePaths.add("http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/t/27/26391_301318.jpg");

        titles = new ArrayList<>();
        titles.add("每日推荐:糖醋排骨");
        titles.add("每日推荐:番茄鸡蛋饼");
        titles.add("每日推荐:腐乳蒜蓉虾");

        categoryList.clear();
        categoryList.add( 0, new Category(R.drawable.ic_chinese_cooking, "家常菜"));
        categoryList.add(1, new Category(R.drawable.ic_fast_cook, "快手菜"));
        categoryList.add(2, new Category(R.drawable.ic_original_cook, "创意菜"));
        categoryList.add(3, new Category(R.drawable.ic_vegetable_cook, "素菜"));
        categoryList.add(4, new Category(R.drawable.ic_cold_cook, "凉菜"));
        categoryList.add(5, new Category(R.drawable.ic_cookie_cook, "烘焙"));
        categoryList.add(6, new Category(R.drawable.ic_noodles_cook, "面食"));
        categoryList.add(7, new Category(R.drawable.ic_soup_cook, "汤"));
        categoryList.add(8, new Category(R.drawable.ic_flavor_cook, "自制调味料"));

        cookList.clear();
        cookList.add(0, new Category(R.drawable.ic_cxhj, "早:葱香花卷"));
        cookList.add(1, new Category(R.drawable.ic_jdg, "早:鸡蛋羹"));
        cookList.add(2, new Category(R.drawable.ic_tdsj, "午:土豆烧鸡"));
        cookList.add(3, new Category(R.drawable.ic_scx, "午:四季豆炒香肠"));
        cookList.add(4, new Category(R.drawable.ic_jgt, "晚:菌菇汤"));
        cookList.add(5, new Category(R.drawable.ic_hsr, "晚:红烧肉"));

    }
    private void initListener() {
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowCookeryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", position + 1);
                bundle.putString("title", categoryList.get(position).getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvMoreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MenuActivity.class);
                startActivity(intent);

            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CookSearchActivity.class);
                startActivity(intent);
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                search_key = titles.get(position).split(":")[1];
//                Log.i(TAG, "search_key: "+search_key);
                OkHttpUtil.sendOkHttpRequest(NetworkUtil.getURL(cookId, search_key, pn, 1), new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
//                        Log.i(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        ShowCookersInfo loadInfo = gson.fromJson(responseData, ShowCookersInfo.class);
//                        Log.i(TAG, "loadInfo: " + loadInfo.toString());
                        info.clear();
                        info.addAll(loadInfo.getResult().getData());
//                        Log.i(TAG, "info: " + info.toString());
                        CooksDBManager.getCooksDBManager(getActivity()).setData(info.get(0));
                        CooksDBManager.getCooksDBManager(getActivity()).insertData(info.get(0));
                        Intent intent = new Intent(getActivity(), CookDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        cookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ShowCookeryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", cookList.get(position).getTitle().split(":")[1]);
                bundle.putString("search_key", cookList.get(position).getTitle().split(":")[1]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    protected void initView() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imagePaths);
        //设置banner动画效果
        //banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        gvCategory.setAdapter(categoryAdapter);
        cookAdapter = new CookAdapter(R.layout.item_main_cooklist, cookList);
        rvRecommend.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvRecommend.setAdapter(cookAdapter);
    }
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
