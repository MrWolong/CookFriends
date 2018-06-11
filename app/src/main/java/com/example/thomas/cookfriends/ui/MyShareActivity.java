package com.example.thomas.cookfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.cookfriends.R;
import com.example.thomas.cookfriends.adapter.ShareFoodAdapter;
import com.example.thomas.cookfriends.bean.CookUser;
import com.example.thomas.cookfriends.bean.Share;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yc on 2018/2/9.
 */

public class MyShareActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.type)
    ImageView type;
    @BindView(R.id.lv_myshare)
    ListView lvMyshare;
    @BindView(R.id.rv_myshare)
    RecyclerView rvMyshare;
    private ShareFoodAdapter shareFoodAdapter;
    private SayingAdapter adapter;
    private List<Share> shareList;
    private CookUser cookUser = BmobUser.getCurrentUser(CookUser.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
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
//        type.setOnClickListener(new View.OnClickListener() {
//            // Tag:1表示列表模式，0表示格子模式
//            @Override
//            public void onClick(View v) {
//                if (type.getTag().equals(0)) {
//                    type.setImageResource(R.drawable.ic_apps);
//                    type.setTag(1);
//                    lvMyshare.setVisibility(View.GONE);
//                    rvMyshare.setVisibility(View.VISIBLE);
//                } else {
//                    type.setImageResource(R.drawable.ic_dehaze);
//                    type.setTag(0);
//                    lvMyshare.setVisibility(View.VISIBLE);
//                    rvMyshare.setVisibility(View.GONE);
//                }
//
//            }
//        });

        lvMyshare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShareDetailActivity.class);
                intent.putExtra("share", shareList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
//        type.setTag(0);
        BmobQuery<Share> query = new BmobQuery<>();
        query.addWhereEqualTo("user", cookUser);// 查询当前用户的所有语录
        query.include("user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if (e == null) {
                    tvSum.setText("共" + list.size() + "篇分享");
                    if (list.size() != 0) {
                        shareList = list;
                        shareFoodAdapter = new ShareFoodAdapter(getApplicationContext(),list);
                        lvMyshare.setAdapter(shareFoodAdapter);
                        //格子模式：recyclerview
//                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
//                        rvMyshare.setLayoutManager(layoutManager);
//                        for(Share share : list) {
//                            Share share1 = new Share();
//                            share1.setContent(share.getContent());
//                            shareList = new ArrayList<>();
//                            shareList.add(share1);
//                        }
//                        adapter = new SayingAdapter(shareList);
//                        rvMyshare.setAdapter(adapter);
                    } else
                        Toast.makeText(getApplicationContext(), "你还未发布任何分享哦", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static class SayingAdapter extends RecyclerView.Adapter<SayingAdapter.ViewHolder>{

        private List<Share> cardList;
        private OnItemClickListener mOnItemClickListener = null;

        static  class ViewHolder extends RecyclerView.ViewHolder {
            View cardView;
            TextView content;

            public ViewHolder(View view) {
                super(view);
                cardView = view;
                content = view.findViewById(R.id.content);
            }
        }

        public SayingAdapter(List<Share> cardList) {
            this.cardList = cardList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_share, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接在adapter设置监听，点击后进行传值、页面跳转
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ShareDetailActivity.class);
//                    it.putExtra("objectId", objectId);
                    v.getContext().startActivity(intent);
                }
            });
            return holder;
        }

        public interface OnItemClickListener {//回调接口
            void onClick(View v);//单击，设置为view是因为我想获得子控件的值
        }

        //定义这个接口的set方法，便于调用

        public void setOnClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onBindViewHolder(final SayingAdapter.ViewHolder holder, final int position) {
            Share share = cardList.get(position);
            holder.content.setText(share.getContent());

            //设置点击和长按事件
            if (mOnItemClickListener != null) {
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onClick(holder.content);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

    }
}
